package com.airtable.interview.airtableschedule.repositories

import com.airtable.interview.airtableschedule.models.Event
import com.airtable.interview.airtableschedule.models.SampleTimelineItems

/**
 * A store for data related to events. Currently, this just returns sample data.
 */
interface EventDataRepository {
    fun getTimelineItems(): List<Event>
}

class EventDataRepositoryImpl : EventDataRepository {
    override fun getTimelineItems(): List<Event> {
        return SampleTimelineItems.timelineItems
    }
}
