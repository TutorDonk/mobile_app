package com.bangkit.tutordonk

import android.app.Application
import com.bangkit.tutordonk.di.appModule
import com.bangkit.tutordonk.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TutorDonkApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@TutorDonkApplication)
            modules(listOf(appModule, networkModule))
        }
    }
}

