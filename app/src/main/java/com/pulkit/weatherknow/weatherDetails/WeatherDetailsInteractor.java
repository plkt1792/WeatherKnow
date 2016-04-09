package com.pulkit.weatherknow.weatherDetails;

import android.text.TextUtils;

import com.google.common.util.concurrent.ListenableFuture;
import com.pulkit.weatherknow.entities.ForecastDetails;
import com.pulkit.weatherknow.network.ExecutorUtils;
import com.pulkit.weatherknow.network.RequestGenerator;
import com.pulkit.weatherknow.network.RequestHandler;
import com.pulkit.weatherknow.preferences.LocationStore;
import com.pulkit.weatherknow.utils.Constants;
import com.pulkit.weatherknow.utils.WeatherApi;
import com.pulkit.weatherknow.weatherDetails.parsers.ParserWeatherForecast;

import java.net.URLEncoder;
import java.util.concurrent.Callable;

import okhttp3.Request;

/**
 * @author pulkit
 */
public class WeatherDetailsInteractor implements IWeatherDetailsInteractor
{
    @Override
    public ListenableFuture<ForecastDetails> getWeatherForecast()
    {
        return ExecutorUtils.getBackgroundPool().submit(new Callable<ForecastDetails>()
        {
            @Override
            public ForecastDetails call() throws Exception
            {
                String url = null;
                if (LocationStore.isLocationAvailable())
                {
                    url = String.format(WeatherApi.URL_WEATHER_FORECAST_BY_LAT_LON, LocationStore.getLatitude(), LocationStore.getLongitude(), WeatherApi.API_KEY);
                } else
                {
                    if (!TextUtils.isEmpty(LocationStore.getCity()))
                    {
                        url = String.format(WeatherApi.URL_WEATHER_FORECAST_BY_CITY, URLEncoder.encode(LocationStore.getCity(), Constants.UTF_8), WeatherApi.API_KEY);
                    } else
                    {
                        url = String.format(WeatherApi.URL_WEATHER_FORECAST_BY_CITY, Constants.DEFAULT_CITY, WeatherApi.API_KEY);
                    }
                }
                Request request = RequestGenerator.get(url);
                String response = RequestHandler.makeRequest(request);
                return ParserWeatherForecast.parse(response);
            }
        });
    }
}
