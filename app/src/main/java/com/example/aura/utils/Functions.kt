package com.example.aura.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

fun formatDate(millis: Long): String {
    return try {
        val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        df.format(Date(millis))
    } catch (_: Exception) {
        millis.toString()
    }
}

/** "Agora", "Há 3 h", "Ontem", "Há 2 dias"... */
fun formatRelativeTime(millis: Long, now: Long = System.currentTimeMillis()): String {
    val diff = (now - millis).coerceAtLeast(0)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
    val hours = TimeUnit.MILLISECONDS.toHours(diff)
    val days = TimeUnit.MILLISECONDS.toDays(diff)
    return when {
        minutes < 1 -> "Agora"
        minutes < 60 -> "Há $minutes min"
        hours < 24 -> "Há $hours h"
        days == 1L -> "Ontem"
        days < 30 -> "Há $days dias"
        else -> formatDate(millis)
    }
}

/** 450 -> "450m", 1200 -> "1.2km" */
fun formatDistance(meters: Int): String =
    if (meters < 1000) "${meters}m"
    else String.format(Locale.US, "%.1fkm", meters / 1000.0)

fun formatRating(rating: Double): String = String.format(Locale.US, "%.1f", rating)
