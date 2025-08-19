package com.airtable.interview.airtableschedule.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airtable.interview.airtableschedule.models.Event
import com.airtable.interview.airtableschedule.models.EventInputModel
import com.airtable.interview.airtableschedule.models.EventModel
import com.airtable.interview.airtableschedule.models.assignLanes
import com.airtable.interview.airtableschedule.models.getDaysBetween
import com.airtable.interview.airtableschedule.models.getRandomColor
import com.airtable.interview.airtableschedule.repositories.EventDataRepository
import com.airtable.interview.airtableschedule.repositories.EventDataRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * ViewModel responsible for managing the state of the timeline screen.
 */
class TimelineViewModel: ViewModel() {
    private val eventDataRepository: EventDataRepository = EventDataRepositoryImpl()

    private val _uiState = MutableStateFlow(TimelineUiState())
    val uiState = _uiState.asStateFlow()

    private var events: List<Event> = eventDataRepository.getTimelineItems()
    private val dateFormatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

    init {
        _uiState.value = TimelineUiState(
            eventLanes = buildEventLaneModels()
        )
    }

    private fun buildEventLaneModels(): List<List<EventModel>> {
        val eventLanes = assignLanes(events)
        return eventLanes.map { lane ->
            lane.map { mapEventModel(it) }
        }
    }

    private fun mapEventModel(event: Event): EventModel {
        return EventModel(
            id = event.id,
            name = event.name,
            startDate = dateFormatter.format(event.startDate),
            endDate = dateFormatter.format(event.endDate),
            durationInDays = getDaysBetween(event.startDate, event.endDate).toInt(),
            color = getRandomColor(),
        )
    }

    fun onEventClick(event: EventModel) {
        _uiState.update { it.copy(selectedEvent = event) }
    }

    fun onEventDialogDismiss() {
        _uiState.update { it.copy(selectedEvent = null) }
    }

    fun onEventSave(eventInput: EventInputModel) {
        val event = events.find { it.id == eventInput.id } ?: return

        val newStartDate = try {
            dateFormatter.parse(eventInput.startDate)
        } catch (e: Exception) {
            event.startDate
        }
        val newEndDate = try {
            dateFormatter.parse(eventInput.endDate)
        } catch (e: Exception) {
            event.endDate
        }
        val updatedEvent = event.copy(
            name = eventInput.name,
            startDate = newStartDate,
            endDate = newEndDate,
        )
        events = events.map {
            if (it.id == updatedEvent.id) updatedEvent else it
        }
        _uiState.update {
            it.copy(
                eventLanes = buildEventLaneModels(),
                selectedEvent = null,
            )
        }
    }
}
