package  com.test.myandroidassignment.network

import  com.test.myandroidassignment.models.weather.response.WeatherInfoResponse
import com.test.myandroidassignment.models.weatherlist.WeatherListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("weather")
    fun callApiForWeatherInfo(@Query("id") cityId: Int): Call<WeatherInfoResponse>

    @GET("onecall")
    fun historicalWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String,
    ): Call<WeatherListResponse>


    @GET("weather")
    fun getCurrentWeatherInfo(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String,
    ): Call<WeatherInfoResponse>
}