package com.pulkit.weatherknow.weatherDetails;

import android.graphics.Bitmap;

import com.google.common.util.concurrent.ListenableFuture;
import com.pulkit.weatherknow.entities.ForecastDetails;
import com.pulkit.weatherknow.entities.WeatherDetails;

import java.util.ArrayList;

/**
 * @author pulkit
 */
public interface IWeatherDetailsInteractor
{
    ListenableFuture<ForecastDetails> getWeatherForecast();
}
