package com.airtable.interview.airtableschedule.models

data class EventInputModel(
    val id: Int,
    val name: String,
    val startDate: String,
    val endDate: String,
)