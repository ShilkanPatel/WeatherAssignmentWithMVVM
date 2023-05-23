package  com.test.myandroidassignment.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.myandroidassignment.common.RequestCompleteListener
import com.test.myandroidassignment.common.kelvinToCelsius
import com.test.myandroidassignment.common.unixTimestampToDateTimeString
import com.test.myandroidassignment.common.unixTimestampToTimeString
import com.test.myandroidassignment.models.weather.WeatherInfoShowModel
import com.test.myandroidassignment.models.weather.response.WeatherData
import com.test.myandroidassignment.models.weather.response.WeatherInfoResponse
import com.test.myandroidassignment.models.weatherlist.WeatherListResponse

class WeatherInfoViewModel : ViewModel() {

    val weatherInfoLiveData = MutableLiveData<WeatherData>()
    val weatherListResponse = MutableLiveData<WeatherListResponse>()
    val weatherInfoFailureLiveData = MutableLiveData<String>()
    val progressBarLiveData = MutableLiveData<Boolean>()

    fun getCurrentWeatherInfo(lat: Double, long: Double, model: WeatherInfoShowModel) {
        progressBarLiveData.postValue(true)

        model.getCurrentLocationWeatherInfo(lat, long, object :
            RequestCompleteListener<WeatherInfoResponse> {
            override fun onRequestSuccess(data: WeatherInfoResponse) {
                val weatherData = WeatherData(
                    dateTime = data.dt.unixTimestampToDateTimeString(),
                    temperature = data.main.temp.kelvinToCelsius().toString(),
                    cityAndCountry = "${data.name}, ${data.sys.country}",
                    weatherConditionIconUrl = "http://openweathermap.org/img/w/${data.weather[0].icon}.png",
                    weatherConditionIconDescription = data.weather[0].description,
                    humidity = "${data.main.humidity}%",
                    pressure = "${data.main.pressure} mBar",
                    visibility = "${data.visibility / 1000.0} KM",
                    sunrise = data.sys.sunrise.unixTimestampToTimeString(),
                    sunset = data.sys.sunset.unixTimestampToTimeString()
                )
                progressBarLiveData.postValue(false)
                weatherInfoLiveData.postValue(weatherData)
            }

            override fun onRequestFailed(errorMessage: String) {
                progressBarLiveData.postValue(false)
                weatherInfoFailureLiveData.postValue(errorMessage)
            }
        })
    }

    fun getWeatherList(lat: Double, long: Double, model: WeatherInfoShowModel) {
        progressBarLiveData.postValue(true)

        model.getWeatherList(lat, long, object :
            RequestCompleteListener<WeatherListResponse> {
            override fun onRequestSuccess(data: WeatherListResponse) {
                progressBarLiveData.postValue(false)
                weatherListResponse.postValue(data)
            }

            override fun onRequestFailed(errorMessage: String) {
                progressBarLiveData.postValue(false)
                weatherInfoFailureLiveData.postValue(errorMessage)
            }
        })
    }
}