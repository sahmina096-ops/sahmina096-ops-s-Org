package com.example

import android.app.Application

class ProteinApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: ProteinApplication
            private set
    }
}
