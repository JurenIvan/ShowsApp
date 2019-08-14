package com.example.shows_jurenivan

import android.app.Application
import com.facebook.stetho.Stetho

class MyShowsApp : Application() {

    companion object {
        lateinit var instance: MyShowsApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Stetho.initializeWithDefaults(this);
    }

}