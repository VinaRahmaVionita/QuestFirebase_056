package com.example.project9

import android.app.Application
import com.example.project9.di.AppContainer
import com.example.project9.di.MahasiswaContainer

class MahasiswaApplications: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container= MahasiswaContainer()
    }
}