package com.lamz.trackinv.utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun getDatesBetween(fromDate: String, toDate: String): List<String> {
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.US)
    val startDate = formatter.parse(fromDate) ?: Date()
    val endDate = formatter.parse(toDate) ?: Date()

    val dates = mutableListOf<String>()
    val calendar = Calendar.getInstance()
    calendar.time = startDate

    while (calendar.time <= endDate) {
        dates.add(formatter.format(calendar.time))
        calendar.add(Calendar.DATE, 1)
    }

    return dates
}

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

fun getFormattedCurrentDate(): String {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return dateFormat.format(calendar.time)
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

