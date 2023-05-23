package  com.test.myandroidassignment.models.weather

import com.test.myandroidassignment.common.RequestCompleteListener
import com.test.myandroidassignment.models.weather.response.WeatherInfoResponse
import com.test.myandroidassignment.models.weatherlist.WeatherListResponse

interface WeatherInfoShowModel {
    fun getWeatherList(
        lat: Double,
        long: Double,
        callback: RequestCompleteListener<WeatherListResponse>
    )

    fun getCurrentLocationWeatherInfo(
        lat: Double,
        long: Double,
        callback: RequestCompleteListener<WeatherInfoResponse>
    )
}