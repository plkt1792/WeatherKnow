package com.pulkit.weatherknow.weatherDetails;

import android.os.Handler;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.pulkit.weatherknow.entities.ForecastDetails;
import com.pulkit.weatherknow.network.ExecutorUtils;
import com.pulkit.weatherknow.preferences.LocationStore;
import com.pulkit.weatherknow.utils.Constants;
import com.pulkit.weatherknow.utils.UtilityClass;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author pulkit
 */
public class WeatherDetailsPresenter implements IWeatherDetailsPresenter
{
    public static final int PERIOD = 30 * 60 * 1000; // 30 minutes
    public static final int DELAY = 100; //100 milli seconds
    private IWeatherForecastView view;
    private IWeatherDetailsInteractor interactor;
    private ListenableFuture<ForecastDetails> future;
    private Timer timer;
    private TimerTask timerTask;
    private Handler handler = new Handler();

    public WeatherDetailsPresenter(IWeatherForecastView weatherDetailsView)
    {
        this.view = weatherDetailsView;
        interactor = new WeatherDetailsInteractor();
    }


    @Override
    public void getWeatherForecast()
    {
        initTimerTask();
        timer = new Timer();
        timer.schedule(timerTask, DELAY, PERIOD);

    }

    @Override
    public void cancelAllTasks()
    {
        ExecutorUtils.cancelService(future);
        timer.cancel();
    }

    private void initTimerTask()
    {
        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        makeForecastCall();
                    }
                });
            }
        };
    }

    private void makeForecastCall()
    {
        view.showProgress();
        future = interactor.getWeatherForecast();
        Futures.addCallback(future, new FutureCallback<ForecastDetails>()
        {
            @Override
            public void onSuccess(ForecastDetails result)
            {
                view.hideProgress();
                if (result != null)
                {
                    updateLocationStore(result);
                    if (result.getWeatherDetails() != null && !result.getWeatherDetails().isEmpty())
                    {
                        result.setTemperatureForecast(UtilityClass.getWeeklyForecast(result.getWeatherDetails()));
                    }
                    view.showForecast(result);
                } else
                {
                    view.showError();
                }
            }

            @Override
            public void onFailure(Throwable t)
            {
                view.hideProgress();
                view.showError();
            }
        }, ExecutorUtils.getUIThread());
    }

    private void updateLocationStore(ForecastDetails details)
    {
        String cityName = details.getCity() + Constants.EMPTY;
        String latitude = details.getLatitude() + Constants.EMPTY;
        String longitude = details.getLongitude() + Constants.EMPTY;
        LocationStore.setLatitude(latitude);
        LocationStore.setLongitude(longitude);
        LocationStore.setCity(cityName);
    }
}
