package com.lamz.trackinv.utils

import com.lamz.trackinv.domain.model.StockData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs



class DoubleExponentialSmoothing(val alpha: Double, val beta: Double) {

    fun predict(data: List<StockData>, forecastPeriods: Int): List<StockData> {
        if (data.isEmpty()) return emptyList()

        var level = data.first().stock
        var trend = if (data.size > 1) data[1].stock - data.first().stock else 0.0

        val smoothedData = mutableListOf<StockData>()
        smoothedData.add(StockData(data.first().date, level))

        for (i in 1 until data.size) {
            val value = data[i].stock
            val lastLevel = level
            level = alpha * value + (1 - alpha) * (level + trend)
            trend = beta * (level - lastLevel) + (1 - beta) * trend

            smoothedData.add(StockData(data[i].date, level))
        }

        val lastDate = data.last().date
        val forecastData = mutableListOf<StockData>()

        for (i in 1..forecastPeriods) {
            val forecast = level + i * trend
            forecastData.add(StockData(incrementDate(lastDate, i), forecast))
        }

        return forecastData
    }

    private fun incrementDate(date: String, days: Int): String {
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        val calendar = Calendar.getInstance()
        calendar.time = formatter.parse(date)!!
        calendar.add(Calendar.DAY_OF_MONTH, days)
        return formatter.format(calendar.time)
    }

    fun calculateMAPE(actualData: List<StockData>, predictedData: List<StockData>): Double {
        val n = actualData.size
        var mape = 0.0
        for (i in 0 until n) {
            val actual = actualData[i].stock
            val predicted = predictedData[i].stock
            if (actual != 0.0) {
                mape += abs((actual - predicted) / actual)
            }
        }
        return (mape / n) * 100
    }

    fun calculateMSE(actualData: List<StockData>, predictedData: List<StockData>): Double {
        val n = actualData.size
        var mse = 0.0
        for (i in 0 until n) {
            val actual = actualData[i].stock
            val predicted = predictedData[i].stock
            mse += (actual - predicted) * (actual - predicted)
        }
        return mse / n
    }
}