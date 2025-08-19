package com.airtable.interview.airtableschedule.models

import androidx.compose.ui.graphics.Color

data class EventModel(
    val name: String,
    val startDate: String,
    val endDate: String,
    val durationInDays: Int,
    val color: Color,
)