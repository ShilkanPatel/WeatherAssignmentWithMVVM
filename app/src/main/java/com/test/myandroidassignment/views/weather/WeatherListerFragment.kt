package com.test.myandroidassignment.views.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.myandroidassignment.common.kelvinToCelsius
import com.test.myandroidassignment.common.unixTimestampToDateTimeString
import com.test.myandroidassignment.databinding.FragmentWeatherListBinding
import com.test.myandroidassignment.models.weather.WeatherInfoShowModel
import com.test.myandroidassignment.models.weather.WeatherInfoShowModelImpl
import com.test.myandroidassignment.models.weather.response.WeatherData
import com.test.myandroidassignment.models.weatherlist.Current
import com.test.myandroidassignment.models.weatherlist.WeatherListResponse
import com.test.myandroidassignment.repository.PrefRepository
import com.test.myandroidassignment.viewmodels.WeatherInfoViewModel
import com.test.myandroidassignment.views.weather.adapter.HourlyAdapter

class WeatherListerFragment : Fragment() {
    private lateinit var model: WeatherInfoShowModel
    private lateinit var viewModel: WeatherInfoViewModel
    private lateinit var binding: FragmentWeatherListBinding
    private var prefRepository: PrefRepository? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = context?.let { WeatherInfoShowModelImpl(it.applicationContext) }!!
        viewModel = ViewModelProviders.of(this)[WeatherInfoViewModel::class.java]
        prefRepository = PrefRepository(requireContext())
        setLiveDataListeners()
    }

    private fun setLiveDataListeners() {
        prefRepository?.let {
            viewModel.getWeatherList(
                it.getLatitude(),
                it.getLongitude(), model)
        }
        viewModel.weatherListResponse.observe(viewLifecycleOwner) { weatherData ->
            setWeatherInfo(weatherData)
        }
    }

    private fun setWeatherInfo(weatherData: WeatherListResponse) {
        binding.apply {
            val layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            rvWeatherList.layoutManager = layoutManager
            val mAppAdapter =
                HourlyAdapter(
                    weatherData.hourly!!,
                    requireContext()
                )
            rvWeatherList.adapter = mAppAdapter
            setWeatherInfo(weatherData.current)
        }
    }

    private fun setWeatherInfo(weatherData: Current) {
        val weatherCurrentData = WeatherData(
            dateTime = weatherData.dt.unixTimestampToDateTimeString(),
            temperature = weatherData.temp.kelvinToCelsius().toString(),
            weatherConditionIconUrl = "http://openweathermap.org/img/w/${weatherData.weather!![0].icon}.png",
            weatherConditionIconDescription = weatherData.weather[0].description,
            humidity = "${weatherData.humidity}%",
            pressure = "${weatherData.pressure} mBar",
            visibility = "${weatherData.visibility / 1000.0} KM",
        )
        binding.apply {

            binding.layoutWeatherBasic.tvDateTime.text = weatherCurrentData.dateTime
            binding.layoutWeatherBasic.tvTemperature.text = weatherCurrentData.temperature
            binding.layoutWeatherBasic.tvCityCountry.text = weatherCurrentData.cityAndCountry
            context?.let {
                Glide.with(it).load(weatherCurrentData.weatherConditionIconUrl)
                    .into(binding.layoutWeatherBasic.ivWeatherCondition)
            }
            binding.layoutWeatherBasic.tvWeatherCondition.text =
                weatherCurrentData.weatherConditionIconDescription

        }
    }

}