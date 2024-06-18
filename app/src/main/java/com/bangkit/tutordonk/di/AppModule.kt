package com.bangkit.tutordonk.di

import com.bangkit.tutordonk.network.ApiServiceProvider
import com.bangkit.tutordonk.network.NetworkClient
import com.bangkit.tutordonk.utils.SharedPreferencesHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { SharedPreferencesHelper(androidContext()) }
}

val networkModule = module {
    single { get<SharedPreferencesHelper>().getToken() }
    single { NetworkClient(get(), get()) }
    single { ApiServiceProvider(get()) }
}