package com.addd.nextformatchat

import android.app.Application
import android.content.Context
import android.os.StrictMode

/**
 * Created by addd on 14.02.2018.
 */
class MyApp : Application() {
    override fun onCreate() {
        MyApp.Companion.instance = this.applicationContext

        super.onCreate()
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
    }

    companion object {
        lateinit var instance: Context
            private set
    }
}