package com.example.tabatatimer.util

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.tabatatimer.Timer


class MyService : Service() {

    override fun onCreate() {
        super.onCreate()
        Log.i("Main", "Service created!")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("Main", "StartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        Log.i("Main", "Destroy")
        super.onDestroy()
    }

}