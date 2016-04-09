package com.pulkit.weatherknow.weatherDetails;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.pulkit.weatherknow.entities.ForecastDetails;
import com.pulkit.weatherknow.entities.WeatherDetails;

import java.util.ArrayList;

/**
 * @author pulkit
 */
public interface IWeatherForecastView
{
    void showProgress();
    void hideProgress();
    void showError();
    void showForecast(@NonNull ForecastDetails forecastList);
}
