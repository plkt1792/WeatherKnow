package com.pulkit.weatherknow.weatherDetails;

import com.pulkit.weatherknow.entities.ForecastDetails;

/**
 * @author pulkit
 */
public interface IWeatherActivityCallback
{
    void showWeatherForecast(ForecastDetails forecastDetails);
}
