package com.airtable.interview.airtableschedule.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airtable.interview.airtableschedule.models.EventModel
import com.airtable.interview.airtableschedule.timeline.ui.EventDetailsDialog

/**
 * A screen that displays a timeline of events.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineScreen(
    viewModel: TimelineViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TimelineView(
        eventLanes = uiState.eventLanes,
        onEventClick = viewModel::onEventClick,
    )

    uiState.selectedEvent?.let { event ->
        ModalBottomSheet(
            onDismissRequest = { viewModel.onEventDialogDismiss() },
        ) {
            EventDetailsDialog(
                event = event,
                onSave = viewModel::onEventSave,
                onClose = { viewModel.onEventDialogDismiss() }
            )
        }
    }
}

/**
 * A view that displays a list of events in swimlanes format.
 *
 * @param eventLanes The list of event lanes.
 */

@Composable
private fun TimelineView(
    eventLanes: List<List<EventModel>>,
    onEventClick: (EventModel) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(eventLanes) { laneEvents ->
                TimelineLane(laneEvents, onEventClick)
            }
        }

        // A vertical timeline axis as a fixed element on the left.
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .width(2.dp)
                .background(Color.Gray)
        )
    }
}

@Composable
private fun TimelineLane(
    events: List<EventModel>,
    onEventClick: (EventModel) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .width(200.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(events) { event ->
            EventCard(event) { onEventClick(event) }
        }
    }
}

/**
 * Renders a single event as a visual card.
 * The card's height is proportional to the event's duration.
 *
 * @param event The event data to display.
 */
@Composable
private fun EventCard(
    event: EventModel,
    onClick: () -> Unit,
) {
    // Scale factor to ensure proportionality while providing a readable minimum height.
    val cardHeight = (event.durationInDays * 20).dp
    val minHeight = 80.dp

    val finalHeight = if (cardHeight < minHeight) minHeight else cardHeight

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(finalHeight)
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = event.color)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxHeight()
        ) {
            Text(
                text = event.name,
                fontSize = 16.sp,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${event.startDate} - ${event.endDate}",
                fontSize = 14.sp,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


