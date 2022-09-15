package com.te6lim.c2candroidassessment

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class ExhibitApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
}