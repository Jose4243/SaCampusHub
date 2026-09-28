package com.mzansi.campushub

import android.app.Application

/**
 * Application subclass whose only job is to apply the user's previously
 * saved dark-mode preference as soon as the process starts, before any
 * Activity is created, so the app opens in the correct theme every launch.
 */
class MzansiApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        LocaleHelper.applyPersistedNightMode(this)
    }
}
