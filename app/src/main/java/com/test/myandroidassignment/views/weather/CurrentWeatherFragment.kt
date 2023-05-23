package com.test.myandroidassignment.views.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.test.myandroidassignment.databinding.FragmentCurrentWeatherBinding
import com.test.myandroidassignment.models.weather.WeatherInfoShowModel
import com.test.myandroidassignment.models.weather.WeatherInfoShowModelImpl
import com.test.myandroidassignment.models.weather.response.WeatherData
import com.test.myandroidassignment.repository.PrefRepository
import com.test.myandroidassignment.viewmodels.WeatherInfoViewModel

class CurrentWeatherFragment : Fragment() {

    private lateinit var model: WeatherInfoShowModel
    private lateinit var viewModel: WeatherInfoViewModel
    private lateinit var binding: FragmentCurrentWeatherBinding
    private var prefRepository: PrefRepository? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)
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
            viewModel.getCurrentWeatherInfo(
                it.getLatitude(),
                it.getLongitude(),
                model
            )
        }
        viewModel.weatherInfoLiveData.observe(viewLifecycleOwner) { weatherData ->
            setWeatherInfo(weatherData)
        }
    }

    private fun setWeatherInfo(weatherData: WeatherData) {
        binding.apply {

            binding.layoutWeatherBasic.tvDateTime.text = weatherData.dateTime
            binding.layoutWeatherBasic.tvTemperature.text = weatherData.temperature
            binding.layoutWeatherBasic.tvCityCountry.text = weatherData.cityAndCountry
            context?.let {
                Glide.with(it).load(weatherData.weatherConditionIconUrl)
                    .into(binding.layoutWeatherBasic.ivWeatherCondition)
            }
            binding.layoutWeatherBasic.tvWeatherCondition.text =
                weatherData.weatherConditionIconDescription

            binding.layoutWeatherAdditional.tvHumidityValue.text = weatherData.humidity
            binding.layoutWeatherAdditional.tvPressureValue.text = weatherData.pressure
            binding.layoutWeatherAdditional.tvVisibilityValue.text = weatherData.visibility

            binding.layoutSunsetSunrise.tvSunriseTime.text = weatherData.sunrise
            binding.layoutSunsetSunrise.tvSunsetTime.text = weatherData.sunset
        }
    }

}