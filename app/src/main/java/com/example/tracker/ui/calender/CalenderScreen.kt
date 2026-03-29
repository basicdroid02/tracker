package com.example.tracker.ui.calender

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tracker.viewModel.PeriodViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodCalendarScreen(
    viewModel: PeriodViewModel,
    onNavigateBack: () -> Unit
) {

    // --- 1. DATE PICKER STATE ---
    val state = rememberDateRangePickerState(
        initialDisplayMode = DisplayMode.Picker,
        selectableDates = object : SelectableDates {

            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= System.currentTimeMillis()
            }

            override fun isSelectableYear(year: Int): Boolean {
                return year <= Calendar.getInstance().get(Calendar.YEAR)
            }
        }
    )

    // --- 2. COLORS ---
    val deepPink = Color(0xFFFF4081)
    val lightPink = Color(0xFFFFCCFF)
    val successGreen = Color(0xFF4CAF50)

    val sdf = remember {
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    }

    // --- 3. VIEWMODEL STATE ---
    val isSaved = viewModel.saveSuccess
    val errorMessage = viewModel.errorMessage
    val conflict = viewModel.overlappingPeriod
    val editingPeriod = viewModel.editingPeriod

    // --- PRE-FILL LOGIC ---
    LaunchedEffect(editingPeriod?.id) {
        if (editingPeriod != null) {
            state.setSelection(editingPeriod.startDate, editingPeriod.endDate)
        } else {
            state.setSelection(null, null)
        }
    }

    // --- SUCCESS STATE ---
    LaunchedEffect(isSaved) {
        if (isSaved) {
            delay(2500)

            if (editingPeriod != null) {
                viewModel.cancelEditing()
                onNavigateBack()
            }

            viewModel.resetSuccess()
        }
    }

    // --- CLEANUP ---
    LaunchedEffect(state.selectedStartDateMillis, state.selectedEndDateMillis) {
        if (isSaved) {
            viewModel.onSelectionChanged()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9FA))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --- HEADER ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (editingPeriod != null) {
                IconButton(onClick = {
                    viewModel.cancelEditing()
                    state.setSelection(null, null)
                    onNavigateBack()
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }

            Text(
                text = if (editingPeriod != null) "Edit Period" else "Track Your Cycle",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            if (editingPeriod != null) {
                TextButton(onClick = {
                    viewModel.cancelEditing()
                    state.setSelection(null, null)
                    onNavigateBack()
                }) {
                    Text("Cancel", color = deepPink)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- ERROR / CONFLICT ---
        AnimatedVisibility(visible = errorMessage != null) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.errorContainer,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Info, contentDescription = null)

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Text(
                            text = errorMessage ?: "",
                            fontWeight = FontWeight.Bold
                        )

                        if (conflict != null) {
                            Text(
                                text = "Overlaps with: ${sdf.format(Date(conflict.startDate))}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- DATE PICKER ---
        Card(
            modifier = Modifier.weight(1f),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            DateRangePicker(
                state = state,
                title = null,
                headline = null,
                showModeToggle = false,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = deepPink,
                    dayInSelectionRangeContainerColor = lightPink.copy(alpha = 0.3f)
                )
            )
        }

        // --- SAVE BUTTON ---
        Button(
            onClick = {
                val start = state.selectedStartDateMillis
                val end = state.selectedEndDateMillis

                if (start != null && end != null) {
                    viewModel.saveSelectedPeriod(start, end)
                }
            },
            enabled = state.selectedEndDateMillis != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSaved) successGreen else deepPink
            )
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                if (isSaved) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Saved Successfully")
                } else {
                    Text(
                        if (editingPeriod != null) "Update Period"
                        else "Log Period"
                    )
                }
            }
        }
    }
}