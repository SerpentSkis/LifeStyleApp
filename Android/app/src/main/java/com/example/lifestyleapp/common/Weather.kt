package com.example.lifestyleapp.common

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.beust.klaxon.Json
import com.beust.klaxon.Klaxon
import java.io.StringReader
import java.net.URL

data class Weather(
    @Json(name = "coord")
    val coord: Coord,
    @Json(name = "main")
    val mainWeather: MainWeather, //this is how the api sends it
    @Json(name = "visibility")
    val visibility: Int,
    @Json(name = "wind")
    val wind: Wind,
    @Json(name = "sys")
    val sys: Sys,
    @Json(name = "name")
    val city: String,
)

data class Coord(
    @Json(name = "lon")
    val longitude: Float,
    @Json(name = "lat")
    val latitude: Float,
)

data class MainWeather(
    @Json(name = "temp")
    val tempKelvin: Float,
    @Json(name = "feels_like")
    val feelsLikeTempKelvin: Float,
    @Json(name = "temp_min")
    val tempMinKelvin: Float,
    @Json(name = "temp_max")
    val tempMaxKelvin: Float,
    @Json(name = "pressure")
    val atmosphericPressure: Int,
    @Json(name = "humidity")
    val humidityPercent: Int,
) {
    //todo better way to get conversions
    fun getTempFahrenheit(): Float = kelvinToFahrenheit(tempKelvin)

    fun getTempCelsius(): Float = kelvinToCelsius(tempKelvin)

    fun getFeelsLikeTempFahrenheit(): Float = kelvinToFahrenheit(feelsLikeTempKelvin)

    fun getFeelsLikeTempCelsius(): Float = kelvinToCelsius(feelsLikeTempKelvin)

    fun getTempMinFahrenheit(): Float = kelvinToFahrenheit(tempMinKelvin)
    fun getTempMaxFahrenheit(): Float = kelvinToFahrenheit(tempMaxKelvin)

    private fun kelvinToFahrenheit(kelvinTemp: Float): Float =
        (kelvinTemp - 273.15F) * (9F / 5F) + 32F

    private fun kelvinToCelsius(kelvinTemp: Float): Float = (kelvinTemp - 273.15F)
}

data class Wind(
    @Json(name = "speed")
    val speedMetersPerSecond: Float,
    @Json(name = "deg")
    val degreesDirection: Float,
)

data class Sys(
    @Json(name = "country")
    val countryCode: String
)

/**
 * provided by openweathermap, Current weather and forecast: Free plan
 * Allowed Calls per minute: 60
 */
fun getWeather(location: Location?): Weather? {
    val url = location?.let { buildURL(it) } ?: return null
    val result: String = URL(url).readText()
    return jsonTextToWeather(result)
}


/**
docs https://openweathermap.org/current
example query
https://api.openweathermap.org/data/2.5/weather?q=Ogden,ut,usa&appid=a742f92606870e1ee06b22a9502b644d
 */
private fun buildURL(location: Location?): String? {
    var url = "https://api.openweathermap.org/data/2.5/weather?q="
    if (!location?.city.isNullOrEmpty()) url += location?.city!!.trim().replace(" ", "%20")
    if (!location?.country.isNullOrEmpty()) {
        url += ",${
            location?.country!!.trim().replace(" ", "%20")
        }"
    }
    if (url == "https://api.openweathermap.org/data/2.5/weather?q=") {
        return null
    }
    val apiKey = "a742f92606870e1ee06b22a9502b644d"
    url += "&appid=$apiKey"
    return url
}

fun jsonTextToWeather(jsonText: String): Weather? {
    if (jsonText.startsWith("ERROR: ")) {
        return null
    }
    return Klaxon().parse<Weather>(reader = StringReader(jsonText))
}

@Entity(tableName = "weather_table")
data class WeatherEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "country")
    var countryCode: String,
    val longitude: Float,
    val latitude: Float,
    val tempKelvin: Float,
    val feelsLikeTempKelvin: Float,
    val tempMinKelvin: Float,
    val tempMaxKelvin: Float,
    val atmosphericPressure: Int,
    val humidityPercent: Int,
    val speedMetersPerSecond: Float,
    val degreesDirection: Float,
)