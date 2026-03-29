package com.example.tracker.ui.calender

import android.content.Context
import android.content.Intent
import android.net.Uri

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.MailOutline

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun HelpScreen() {

    val scrollState = rememberScrollState()
    val deepPink = Color(0xFFFF4081)
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9FA))
            .verticalScroll(scrollState)
            .padding(all = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --- HEADER ---
        Text(
            text = "How can we help?",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )

        Text(
            text = "Common questions about tracking your cycle.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )

        // --- FAQ ---
        HelpItem(
            "How do I log a new period?",
            "Go to the Calendar tab. Select start and end dates, then tap Save Period."
        )

        HelpItem(
            "What is Cycle Length?",
            "Days between first day of one period and next. Usually 21–35 days."
        )

        HelpItem(
            "How do I delete an entry?",
            "Go to History tab and tap delete (X) icon on a record."
        )

        HelpItem(
            "Where is my data stored?",
            "All data is stored locally on your device. No cloud, no tracking."
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- CONTACT CARD ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Still have questions?",
                    fontWeight = FontWeight.Bold,
                    color = deepPink
                )

                Text(
                    text = "Our team is happy to help you.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp, bottom = 20.dp)
                )

                Button(
                    onClick = { contactSupport(context) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = deepPink)
                ) {
                    Icon(Icons.Outlined.MailOutline, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Contact Support", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun HelpItem(question: String, answer: String) {

    var isExpanded by remember { mutableStateOf(false) }
    val deepPink = Color(0xFFFF4081)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = question,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = if (isExpanded)
                        Icons.Default.KeyboardArrowUp
                    else
                        Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = deepPink
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Text(
                    text = answer,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
    }
}

// --- CONTACT SUPPORT FUNCTION ---
fun contactSupport(context: Context) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:support@tracker.com")
        putExtra(Intent.EXTRA_SUBJECT, "Support Request")
    }
    context.startActivity(intent)
}