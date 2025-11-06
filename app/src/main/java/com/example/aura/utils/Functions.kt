package com.example.aura.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(millis: Long): String {
    return try {
        val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        df.format(Date(millis))
    } catch (_: Exception) {
        millis.toString()
    }
}