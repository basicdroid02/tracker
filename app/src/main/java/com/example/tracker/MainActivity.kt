package com.example.tracker

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.tracker.ui.calender.PeriodCalendarScreen

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.unit.dp
import com.example.tracker.ui.calender.AboutScreen
import com.example.tracker.ui.calender.HelpScreen
import com.example.tracker.ui.calender.HistoryScreen
import com.example.tracker.viewModel.PeriodViewModel
import com.example.tracker.viewModel.PeriodViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private  val viewModel: PeriodViewModel by viewModels {
        PeriodViewModelFactory((application as MainApplication).repository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {

                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                // Track current screen
                var currentScreen by remember { mutableStateOf("calendar") }

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {

                            Text(
                                "Period Tracker",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(16.dp)
                            )

                            HorizontalDivider()

                            // 1. CALENDAR ITEM
                            NavigationDrawerItem(
                                label = { Text("Calendar") },
                                selected = currentScreen == "calendar",
                                onClick = {
                                    currentScreen = "calendar"
                                    scope.launch { drawerState.close() }
                                }
                            )

                            // 2. HISTORY ITEM
                            NavigationDrawerItem(
                                label = { Text("History") },
                                selected = currentScreen == "history",
                                onClick = {
                                    viewModel.cancelEditing()
                                    currentScreen = "history"
                                    scope.launch { drawerState.close() }
                                }
                            )

                            // 3. ABOUT ITEM
                            NavigationDrawerItem(
                                label = { Text("About") },
                                selected = currentScreen == "about",
                                onClick = {
                                    currentScreen = "about"
                                    scope.launch { drawerState.close() }
                                }
                            )

                            // 4. HELP ITEM (Placeholder)
                            NavigationDrawerItem(
                                label = { Text("Help") },
                                selected = currentScreen == "help",
                                onClick = {
                                    currentScreen = "help"
                                    scope.launch { drawerState.close() }
                                }
                            )
                        }
                    }
                ) {

                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        when (currentScreen) {
                                            "calendar" -> "Tracker"
                                            "history" -> "History"
                                            "about" -> "About"
                                            else -> "Help"
                                        }
                                    )
                                },
                                navigationIcon = {
                                    IconButton(
                                        onClick = {
                                            scope.launch { drawerState.open() }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Menu,
                                            contentDescription = "Menu"
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = Color(0xFFFFCCFF)
                                )
                            )
                        }
                    ) { innerPadding ->

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {

                            // 5. SWITCH BETWEEN SCREENS
                            when (currentScreen) {

                                "calendar" -> PeriodCalendarScreen(
                                    viewModel = viewModel,
                                    onNavigateBack = {
                                        currentScreen = "history"
                                    }
                                )

                                "history" -> HistoryScreen(
                                    viewModel = viewModel,
                                    onNavigateToCalendar = {
                                        currentScreen = "calendar" // <-- THE FIX
                                    }
                                )

                                "about" -> AboutScreen()

                                "help" -> HelpScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}