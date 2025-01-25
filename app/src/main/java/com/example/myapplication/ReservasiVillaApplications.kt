package com.example.myapplication

import android.app.Application
import com.example.myapplication.container.AppContainer
import com.example.myapplication.container.ReservasiVillaContainer

class ReservasiVillaApplications: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = ReservasiVillaContainer()
    }
}