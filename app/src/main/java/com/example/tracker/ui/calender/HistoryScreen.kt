package com.example.tracker.ui.calender

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tracker.data.local.entity.PeriodEntity
import com.example.tracker.ui.utils.verticalScrollbar
import com.example.tracker.viewModel.PeriodViewModel
import android.content.Intent
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: PeriodViewModel,
    onNavigateToCalendar: () -> Unit
) {
    val allPeriods by viewModel.allPeriods.collectAsState(initial = emptyList())
    val editingPeriod = viewModel.editingPeriod

    val dayFormat = SimpleDateFormat("dd", Locale.getDefault())
    val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())

    val scrollState = rememberScrollState()
    val context = LocalContext.current

    var selectedPeriods by remember { mutableStateOf(setOf<PeriodEntity>()) }
    var isSelectionMode by remember { mutableStateOf(false) }

    val signaturePink = Color(0xFFFF8DA1)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = if (isSelectionMode)
                        "${selectedPeriods.size} Selected"
                    else "Cycle History",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            actions = {
                IconButton(
                    onClick = { exportHistory(context, allPeriods) },
                    enabled = allPeriods.isNotEmpty()
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, tint = signaturePink)
                }

                if (isSelectionMode) {
                    IconButton(onClick = {
                        selectedPeriods.forEach { viewModel.deletePeriod(it) }
                        selectedPeriods = emptySet()
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                    }


                    IconButton(onClick = {
                        isSelectionMode = false
                        selectedPeriods = emptySet()
                    }) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.White
            )
        )

        AnimatedVisibility(visible = editingPeriod != null) {
            Surface(
                color = Color(0xFFFFF1F3),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Editing...",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        if (allPeriods.isEmpty()) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Your logged cycles will appear here.", color = Color.LightGray)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScrollbar(scrollState)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp)
            ) {

                val sortedList = allPeriods.sortedByDescending { it.startDate }

                sortedList.forEachIndexed { index, period ->

                    val startDate = Date(period.startDate)
                    val durationDays =
                        ((period.endDate - period.startDate) / (24 * 60 * 60 * 1000)).toInt() + 1
                    val isSelected = selectedPeriods.contains(period)

                    val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
                    val currentYear = yearFormat.format(startDate)
                    val prevYear =
                        if (index > 0) yearFormat.format(Date(sortedList[index - 1].startDate)) else ""

                    if (currentYear != prevYear) {
                        Text(
                            text = yearFormat.format(startDate),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 32.dp, bottom = 12.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(45.dp)
                        ) {
                            Text(
                                dayFormat.format(startDate),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                monthFormat.format(startDate).uppercase(Locale.getDefault()),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.LightGray
                            )
                        }

                        Spacer(Modifier.width(12.dp))

                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 6.dp)
                                .combinedClickable(
                                    onLongClick = {
                                        isSelectionMode = true
                                        selectedPeriods = selectedPeriods + period
                                    },
                                    onClick = {
                                        if (isSelectionMode) {
                                            selectedPeriods =
                                                if (isSelected) selectedPeriods - period
                                                else selectedPeriods + period

                                            if (selectedPeriods.isEmpty())
                                                isSelectionMode = false
                                        }
                                    }
                                ),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected)
                                    Color(0xFFFFF5F6)
                                else Color(0xFFFBFBFB)
                            ),
                            border = if (isSelected)
                                BorderStroke(1.dp, signaturePink)
                            else BorderStroke(0.5.dp, Color(0xFFEEEEEE)),
                            elevation = CardDefaults.cardElevation(0.dp)
                        ) {

                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Box(
                                    Modifier
                                        .width(4.dp)
                                        .height(30.dp)
                                        .background(signaturePink, CircleShape)
                                )

                                Spacer(Modifier.width(12.dp))

                                Column(Modifier.weight(1f)) {
                                    Text(
                                        "Period Duration",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.Gray
                                    )
                                    Text(
                                        "$durationDays Days",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                if (isSelectionMode) {
                                    Checkbox(
                                        checked = isSelected,
                                        onCheckedChange = {
                                            selectedPeriods =
                                                if (isSelected) selectedPeriods - period
                                                else selectedPeriods + period
                                        },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = signaturePink
                                        )
                                    )
                                } else {
                                    Row {
                                        IconButton(onClick = {
                                            viewModel.startEditing(period)
                                            onNavigateToCalendar()
                                        }) {
                                            Icon(
                                                Icons.Default.Edit,
                                                contentDescription = null,
                                                tint = Color.LightGray
                                            )
                                        }

                                        IconButton(onClick = {
                                            viewModel.deletePeriod(period)
                                        }) {
                                            Icon(
                                                Icons.Default.Close,
                                                contentDescription = null,
                                                tint = Color.LightGray
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (index < sortedList.size - 1) {
                        val cycleLength =
                            ((period.startDate - sortedList[index + 1].startDate) /
                                    (24 * 60 * 60 * 1000)).toInt()

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 20.dp)
                        ) {

                            Box(
                                Modifier
                                    .width(1.dp)
                                    .height(20.dp)
                                    .background(Color(0xFFEEEEEE))
                            )

                            Spacer(Modifier.width(36.dp))

                            Surface(
                                color = getCycleColor(cycleLength).copy(alpha = 0.1f),
                                shape = RoundedCornerShape(100)
                            ) {
                                Text(
                                    "$cycleLength day cycle",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Medium,
                                    color = getCycleTextColor(cycleLength)
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(50.dp))
            }
        }
    }
}

fun exportHistory(context: Context, periods: List<PeriodEntity>) {
    val csvHeader = "Start Date,End Date,Duration (Days)\n"
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val csvData = periods.joinToString("\n") { period ->
        val duration =
            ((period.endDate - period.startDate) / (24 * 60 * 60 * 1000)).toInt() + 1
        "${sdf.format(Date(period.startDate))},${sdf.format(Date(period.endDate))},$duration"
    }

    val fileName = "period_history.csv"

    try {
        val file = File(context.cacheDir, fileName)
        file.writeText(csvHeader + csvData)

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(intent, "Download History"))

    } catch (_: Exception) {
        Toast.makeText(
            context,
            "Export failed",
            Toast.LENGTH_SHORT
        ).show()
    }
}

fun getCycleColor(days: Int): Color {
    return when {
        days < 21 -> Color(0xFFFFAAB1).copy(alpha = 0.4f)
        days > 30 -> Color(0xFFCE93D8).copy(alpha = 0.4f)
        else -> Color(0xFFB2DFDB).copy(alpha = 0.3f)
    }
}

fun getCycleTextColor(days: Int): Color {
    return when {
        days < 21 -> Color(0xFFFB6B6C)
        days > 30 -> Color(0xFF6A1B9A)
        else -> Color(0xFF00796B)
    }
}