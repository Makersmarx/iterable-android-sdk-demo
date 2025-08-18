package com.example.iterable

import android.app.Application
import com.iterable.iterableapi.IterableApi
import com.iterable.iterableapi.IterableConfig



class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val apiKey = "removed for privacy"
        val configBuilder = IterableConfig.Builder()


        IterableApi.initialize(this, apiKey, configBuilder.build())
    }
}