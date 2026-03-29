package com.example.tracker.ui.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

fun contactSupport(context: Context) {
    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf("support@yourapp.com"))
        putExtra(Intent.EXTRA_SUBJECT, "App Support: Cycle Harmony")
    }

    try {
        context.startActivity(
            Intent.createChooser(emailIntent, "Send Feedback Via...")
        )
    } catch (e: Exception) {
        Toast.makeText(
            context,
            "No email client found.",
            Toast.LENGTH_SHORT
        ).show()
    }
}