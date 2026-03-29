package com.example.tracker.ui.calender
import android.content.Context
import android.content.Intent
import android.net.Uri

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star

import androidx.compose.material3.*

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun AboutScreen() {

    val scrollState = rememberScrollState()
    val deepPink = Color(0xFFFF4081)
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(all = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --- 1. BRANDING SECTION ---
        Spacer(modifier = Modifier.height(32.dp))

        Surface(
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
            color = deepPink.copy(alpha = 0.1f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text("🌸", fontSize = 40.sp)
            }
        }

        Text(
            text = "Period Tracker",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = "Version 1.0.0",
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- 2. VISION CARD ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(size = 16.dp)
        ) {
            Column(modifier = Modifier.padding(all = 20.dp)) {

                Text(
                    text = "Our Vision",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = deepPink
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Empowering you to understand your body's natural rhythm through simple, beautiful, and private tracking.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- 3. FEATURES LIST ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        ) {

            Text(
                text = "Key Features",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            FeatureItem(
                title = "Paginated Calendar",
                description = "Easily mark your start and end dates month-by-month."
            )

            FeatureItem(
                title = "Visual Timeline",
                description = "A clean history showing cycle lengths and durations."
            )

            FeatureItem(
                title = "Privacy Focused",
                description = "All data is stored locally. No cloud, no tracking."
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- 4. ACTION BUTTON ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Button(
                onClick = { openPlayStore(context) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(size = 12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = deepPink)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Rate App", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- 5. PRIVACY FOOTER ---
        Text(
            text = "Designed for Privacy",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )

        Text(
            text = "This app does not collect, store, or share your personal health data. Everything remains on your device.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall,
            color = Color.LightGray,
            modifier = Modifier.padding(top = 4.dp, bottom = 40.dp)
        )
    }
}

@Composable
fun FeatureItem(title: String, description: String) {
    Row(modifier = Modifier.padding(vertical = 8.dp)) {

        Text("•", color = Color(0xFFFF4081), fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

fun openPlayStore(context: Context) {
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("market://details?id=${context.packageName}")
    )

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        // fallback to browser
    }
}