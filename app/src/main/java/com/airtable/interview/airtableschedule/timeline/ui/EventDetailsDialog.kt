package com.airtable.interview.airtableschedule.timeline.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airtable.interview.airtableschedule.models.EventInputModel
import com.airtable.interview.airtableschedule.models.EventModel

@Composable
fun EventDetailsDialog(
    event: EventModel,
    onSave: (EventInputModel) -> Unit,
    onClose: () -> Unit,
) {
    // State to hold the editable values in the dialog
    var name by remember { mutableStateOf(event.name) }
    var startDateText by remember { mutableStateOf(event.startDate) }
    var endDateText by remember { mutableStateOf(event.endDate) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Edit Event",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Event Name") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = startDateText,
            onValueChange = { startDateText = it },
            label = { Text("Start Date (MM/DD/YYYY)") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = endDateText,
            onValueChange = { endDateText = it },
            label = { Text("End Date (MM/DD/YYYY)") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = onClose,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text("Cancel")
            }
            Button(
                onClick = {
                    onSave(
                        EventInputModel(
                            id = event.id,
                            name = name,
                            startDate = startDateText,
                            endDate = endDateText,
                        )
                    )
                }
            ) {
                Text("Save")
            }
        }
    }
}