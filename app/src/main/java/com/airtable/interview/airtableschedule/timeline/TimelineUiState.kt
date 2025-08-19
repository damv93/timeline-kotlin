package com.airtable.interview.airtableschedule.timeline

import com.airtable.interview.airtableschedule.models.EventModel

/**
 * UI state for the timeline screen.
 */
data class TimelineUiState(
    val eventLanes: List<List<EventModel>> = emptyList(),
    val selectedEvent: EventModel? = null,
)
