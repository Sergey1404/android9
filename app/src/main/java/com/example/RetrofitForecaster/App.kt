package com.example.RetrofitForecaster

import android.app.Application
import com.example.RetrofitForecaster.data.WeatherAPI
import timber.log.Timber

class App : Application() {
    companion object {
        lateinit var weatherAPI: WeatherAPI
    }

    override fun onCreate() {
        super.onCreate()
        weatherAPI = WeatherAPI.createAPI()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree());
        }
    }
}