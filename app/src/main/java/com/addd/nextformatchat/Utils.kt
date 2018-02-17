package com.addd.nextformatchat

import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.gson.Gson
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by addd on 14.02.2018.
 */
val APP_TOKEN = "token"
val ID_CHAT = "idChat"
val ID_USER = "idUser"
val gson = Gson()
lateinit var myWebSocket: MyWebSocket
fun AppCompatActivity.toast(s: String) {
    Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.toast(@StringRes r: Int) {
    Toast.makeText(applicationContext, r, Toast.LENGTH_SHORT).show()
}


val ONE_MINUTE_IN_MILLIS = 60000
val backendDateFormatTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.US)
val abackendDateFormatTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
private val normalFormatTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
fun formatTime(date: String): String {
    return try {
        val hoursZ = localTime.substring(0, 3).toInt()
        val minutesZ = (localTime.substring(0, 1) + localTime.substring(3, localTime.length)).toInt() + hoursZ * 60
        val date1 = backendDateFormatTime.parse(date)
        val t = date1.time
        val afterAdding = Date(t + (minutesZ * ONE_MINUTE_IN_MILLIS))
        normalFormatTime.format(afterAdding)
    } catch (e: ParseException) {
        e.printStackTrace()
        try {
            val hoursZ = localTime.substring(0, 3).toInt()
            val minutesZ = (localTime.substring(0, 1) + localTime.substring(3, localTime.length)).toInt() + hoursZ * 60
            val date1 = abackendDateFormatTime.parse(date)
            val t = date1.time
            val afterAdding = Date(t + (minutesZ * ONE_MINUTE_IN_MILLIS))
            normalFormatTime.format(afterAdding)
        } catch (e: ParseException) {
            "8888.88.88"
        }
    }
}

val normalDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
fun formatDate(date: String): String {
    return try {
        normalDateFormat.format(backendDateFormatTime.parse(date))
    } catch (e: ParseException) {
        e.printStackTrace()
        "8888.88.88"
    }
}

val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault())
val currentLocalTime = calendar.time
val dateZ = SimpleDateFormat("Z")
val localTime = dateZ.format(currentLocalTime)