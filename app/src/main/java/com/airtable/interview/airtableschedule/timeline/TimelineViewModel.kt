package com.airtable.interview.airtableschedule.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airtable.interview.airtableschedule.models.Event
import com.airtable.interview.airtableschedule.models.EventModel
import com.airtable.interview.airtableschedule.models.assignLanes
import com.airtable.interview.airtableschedule.models.getDaysBetween
import com.airtable.interview.airtableschedule.models.getRandomColor
import com.airtable.interview.airtableschedule.repositories.EventDataRepository
import com.airtable.interview.airtableschedule.repositories.EventDataRepositoryImpl
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * ViewModel responsible for managing the state of the timeline screen.
 */
class TimelineViewModel: ViewModel() {
    private val eventDataRepository: EventDataRepository = EventDataRepositoryImpl()

    val uiState: StateFlow<TimelineUiState> = eventDataRepository
        .getTimelineItems()
        .map(::buildUiState)
        .stateIn(
            viewModelScope,
            initialValue = TimelineUiState(),
            started = SharingStarted.WhileSubscribed()
        )

    private fun buildUiState(events: List<Event>): TimelineUiState {
        val dateFormatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val eventLanes = assignLanes(events)
        val eventLaneModels = eventLanes.map { lane ->
            lane.map {
                EventModel(
                    name = it.name,
                    startDate = dateFormatter.format(it.startDate),
                    endDate = dateFormatter.format(it.endDate),
                    durationInDays = getDaysBetween(it.startDate, it.endDate).toInt(),
                    color = getRandomColor(),
                )
            }
        }
        return TimelineUiState(eventLaneModels)
    }
}
