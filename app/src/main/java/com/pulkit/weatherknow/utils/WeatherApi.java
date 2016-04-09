package com.pulkit.weatherknow.utils;

/**
 * @author pulkit
 */
public class WeatherApi
{
    public static final String API_KEY ="084700c595b763663636fda9b8bbffae";

    public static final String URL_WEATHER_FORECAST_BY_CITY = "http://api.openweathermap.org/data/2.5/forecast?q=%s&appid=%s";
    public static final String URL_WEATHER_FORECAST_BY_LAT_LON = "http://api.openweathermap.org/data/2.5/forecast?lat=%s&lon=%s&appid=%s";

    public static final String URL_CURRENT_WEATHER_BY_CITY = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";
    public static final String URL_CURRENT_WEATHER_BY_LAT_LON = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s";
}
