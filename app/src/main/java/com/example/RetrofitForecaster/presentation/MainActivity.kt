package com.example.RetrofitForecaster.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.RetrofitForecaster.App
import com.example.RetrofitForecaster.Constants.API_CITY
import com.example.RetrofitForecaster.Constants.API_KEY
import com.example.RetrofitForecaster.Constants.API_LANG
import com.example.RetrofitForecaster.Constants.API_UNITS
import com.example.RetrofitForecaster.Constants.TIMBER_TAG
import com.example.RetrofitForecaster.data.WeatherNW
import com.example.RetrofitForecaster.data.WeatherStore
import com.example.RetrofitForecaster.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private val weatherAdapter = WeatherAdapter()
    private val weatherAPI = App.weatherAPI
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recycleViewInit()
        if (WeatherStore.weatherList.isEmpty()) {
            loadWeather()
            Timber.tag(TIMBER_TAG).d("Залез в сеть.")
        } else {
            weatherAdapter.submitList(WeatherStore.weatherList)
            Timber.tag(TIMBER_TAG).d("Восстановил из WeatherStore")
        }
    }

    private fun recycleViewInit() {
        binding.rvWeather.adapter = weatherAdapter
        binding.rvWeather.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun loadWeather() {
        weatherAPI.getForecast(API_CITY, API_KEY, API_UNITS, API_LANG)
            .enqueue(object : Callback<WeatherNW> {
                override fun onResponse(call: Call<WeatherNW>, response: Response<WeatherNW>) {
                    if (response.isSuccessful) {
                        WeatherStore.weatherList = response.body()?.list!!
                        weatherAdapter.submitList(WeatherStore.weatherList)
                    }
                }

                override fun onFailure(call: Call<WeatherNW>, trowable: Throwable) {
                    Timber.tag(TIMBER_TAG).e(trowable)
                }
            })
    }
}
