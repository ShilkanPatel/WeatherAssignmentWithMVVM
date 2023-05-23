package  com.test.myandroidassignment.models.weather

import android.content.Context
import com.test.myandroidassignment.BuildConfig
import com.test.myandroidassignment.common.RequestCompleteListener
import com.test.myandroidassignment.models.weather.response.WeatherInfoResponse
import com.test.myandroidassignment.models.weatherlist.WeatherListResponse
import com.test.myandroidassignment.network.ApiInterface
import com.test.myandroidassignment.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherInfoShowModelImpl(private val context: Context) : WeatherInfoShowModel {
    override fun getWeatherList(
        lat: Double,
        long: Double,
        callback: RequestCompleteListener<WeatherListResponse>
    ) {
        val apiInterface: ApiInterface = RetrofitClient.client.create(ApiInterface::class.java)
        val call: Call<WeatherListResponse> =
            apiInterface.historicalWeatherData(lat, long, BuildConfig.APP_ID)

        call.enqueue(object : Callback<WeatherListResponse> {
            override fun onResponse(
                call: Call<WeatherListResponse>,
                response: Response<WeatherListResponse>
            ) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!)
                else
                    callback.onRequestFailed(response.message())
            }

            override fun onFailure(call: Call<WeatherListResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
            }
        })
    }

    override fun getCurrentLocationWeatherInfo(
        lat: Double,
        long: Double,
        callback: RequestCompleteListener<WeatherInfoResponse>
    ) {
        val apiInterface: ApiInterface = RetrofitClient.client.create(ApiInterface::class.java)
        val call: Call<WeatherInfoResponse> =
            apiInterface.getCurrentWeatherInfo(lat, long, BuildConfig.APP_ID)

        call.enqueue(object : Callback<WeatherInfoResponse> {
            override fun onResponse(
                call: Call<WeatherInfoResponse>,
                response: Response<WeatherInfoResponse>
            ) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!)
                else
                    callback.onRequestFailed(response.message())
            }

            override fun onFailure(call: Call<WeatherInfoResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!)
            }
        })
    }
}