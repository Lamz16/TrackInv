package com.lamz.trackinv.utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun convertStringToCalendar(dateTimeString: String): Calendar {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val calendar = Calendar.getInstance()

    try {
        val date = dateFormat.parse(dateTimeString)
        calendar.time = date
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return calendar
}

fun formatToCurrency(value: String): String {
    val cleanString = value.replace("[^\\d]".toRegex(), "")

    return if (cleanString.isNotEmpty()) {
        val formatter = DecimalFormat("#,###")
        formatter.format(cleanString.toLong())
    } else {
        ""
    }
}

