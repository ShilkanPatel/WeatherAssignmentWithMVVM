package com.test.myandroidassignment.models.weatherlist

data class WeatherListResponse(
    val timezone: String = "",
    val timezoneOffset: Int = 0,
    val daily: List<DailyItem>?,
    val lon: Double = 0.0,
    val hourly: ArrayList<HourlyItem>?,
    val current: Current,
    val lat: Double = 0.0
)

data class Current(
    val sunrise: Int = 0,
    val temp: Double = 0.0,
    val visibility: Int = 0,
    val uvi: Double = 0.0,
    val pressure: Int = 0,
    val clouds: Int = 0,
    val feelsLike: Double = 0.0,
    val windGust: Double = 0.0,
    val dt: Int = 0,
    val windDeg: Int = 0,
    val dewPoint: Double = 0.0,
    val sunset: Int = 0,
    val weather: List<WeatherItem>?,
    val humidity: Int = 0,
    val windSpeed: Double = 0.0
)

data class FeelsLike(
    val eve: Double = 0.0,
    val night: Double = 0.0,
    val day: Double = 0.0,
    val morn: Double = 0.0
)


data class Temp(
    val min: Double = 0.0,
    val max: Double = 0.0,
    val eve: Double = 0.0,
    val night: Double = 0.0,
    val day: Double = 0.0,
    val morn: Double = 0.0
)


data class WeatherItem(
    val icon: String = "",
    val description: String = "",
    val main: String = "",
    val id: Int = 0
)


data class DailyItem(
    val moonset: Int = 0,
    val rain: Double = 0.0,
    val sunrise: Int = 0,
    val temp: Temp,
    val moonPhase: Double = 0.0,
    val uvi: Double = 0.0,
    val moonrise: Int = 0,
    val pressure: Int = 0,
    val clouds: Int = 0,
    val feelsLike: FeelsLike,
    val windGust: Double = 0.0,
    val dt: Int = 0,
    val pop: Double = 0.0,
    val windDeg: Int = 0,
    val dewPoint: Double = 0.0,
    val sunset: Int = 0,
    val weather: List<WeatherItem>?,
    val humidity: Int = 0,
    val windSpeed: Double = 0.0
)


data class HourlyItem(
    val temp: Double = 0.0,
    val visibility: Int = 0,
    val uvi: Double = 0.0,
    val pressure: Int = 0,
    val clouds: Int = 0,
    val feelsLike: Double = 0.0,
    val windGust: Double = 0.0,
    val dt: Int = 0,
    val pop: Int = 0,
    val windDeg: Int = 0,
    val dewPoint: Double = 0.0,
    val weather: List<WeatherItem>?,
    val humidity: Int = 0,
    val windSpeed: Double = 0.0
)


