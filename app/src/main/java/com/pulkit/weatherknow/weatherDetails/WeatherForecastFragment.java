package com.pulkit.weatherknow.weatherDetails;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pulkit.weatherknow.R;
import com.pulkit.weatherknow.entities.ForecastDetails;
import com.pulkit.weatherknow.entities.TempType;
import com.pulkit.weatherknow.entities.Temperature;
import com.pulkit.weatherknow.entities.WeatherDetails;
import com.pulkit.weatherknow.utils.Constants;
import com.pulkit.weatherknow.utils.UtilityClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author pulkit
 */
public class WeatherForecastFragment extends Fragment implements IWeatherActivityCallback
{
    public static final String MIN = "Min: ";
    public static final String MAX = "Max: ";
    public static final String DD_MM_YYYY = "dd-MM-yyyy";
    public static final String HH_MM_A = "hh:mm a";
    public static final String FORECAST_DETAILS = "forecast_details";

    @Bind(R.id.weather_description)
    TextView weatherDescription;
    @Bind(R.id.max_temp)
    TextView maxTemp;
    @Bind(R.id.min_temp)
    TextView minTemp;
    @Bind(R.id.current_temp)
    TextView currentTemp;

    @Bind(R.id.hourly_forecast)
    LinearLayout hourlyForecast;
    @Bind(R.id.five_day_forecast)
    LinearLayout fiveDayForecast;

    private ForecastDetails mForecastDetails;
    private WeatherDetails mCurrentWeatherDetails;
    private TempType mTempType = TempType.C;
    private Callbacks mCallback;

    public static WeatherForecastFragment newInstance(ForecastDetails details)
    {
        WeatherForecastFragment fragment = new WeatherForecastFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(FORECAST_DETAILS, details);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        mForecastDetails = (ForecastDetails) getArguments().getSerializable(FORECAST_DETAILS);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity)
    {
        if (activity instanceof Callbacks)
        {
            mCallback = (Callbacks) activity;
        } else
        {
            throw new RuntimeException(new StringBuilder(5).append(activity.getClass().getSimpleName()).append(" must implement ").append(Callbacks.class.getSimpleName()).toString());
        }
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_weather_forecast, null, false);
        ButterKnife.bind(this, view);
        if (mForecastDetails != null)
        {
            showForecast(mForecastDetails);
        }
        return view;
    }

    private void initCurrentWeather()
    {
        if (mCurrentWeatherDetails != null)
        {
            weatherDescription.setText(mCurrentWeatherDetails.getWeatherDescription().toUpperCase());

            String tempMax = MAX + UtilityClass.getTemp(mCurrentWeatherDetails.getMaxTemp(), mTempType);
            maxTemp.setText(tempMax);

            String tempMin = MIN + UtilityClass.getTemp(mCurrentWeatherDetails.getMinTemp(), mTempType);
            minTemp.setText(tempMin);

            currentTemp.setText(UtilityClass.getTemp(mCurrentWeatherDetails.getCurrentTemp(), mTempType));
        } else
        {
            //else do nothing
        }
    }

    private void initHourlyForecast()
    {
        if (mForecastDetails != null)
        {
            ArrayList<WeatherDetails> forecastList = mForecastDetails.getWeatherDetails();
            if (forecastList != null && !forecastList.isEmpty())
            {
                hourlyForecast.removeAllViews();
                for (WeatherDetails weatherDetails : forecastList)
                {
                    View hourlyForecastView = getActivity().getLayoutInflater().inflate(R.layout.hourly_forecast_item, null, false);
                    TextView tvDate = (TextView) hourlyForecastView.findViewById(R.id.date);
                    TextView hour = (TextView) hourlyForecastView.findViewById(R.id.hour);
                    TextView temp = (TextView) hourlyForecastView.findViewById(R.id.temp);
                    Date date = UtilityClass.getDateFromDateTxt(weatherDetails.getDateTxt());
                    if (date != null)
                    {
                        DateFormat dateFormatter = new SimpleDateFormat(DD_MM_YYYY, Locale.US);
                        dateFormatter.setLenient(false);
                        String currentDate = dateFormatter.format(date);
                        String setDate = currentDate + Constants.EMPTY;
                        tvDate.setText(setDate);

                        dateFormatter = new SimpleDateFormat(HH_MM_A, Locale.US);
                        dateFormatter.setLenient(false);
                        String currentTime = dateFormatter.format(date);
                        String setTime = currentTime + Constants.EMPTY;
                        hour.setText(setTime);
                    }
                    temp.setText(UtilityClass.getTemp(weatherDetails.getCurrentTemp(), mTempType));
                    hourlyForecast.addView(hourlyForecastView);
                }

            } else
            {
                //do nothing
            }
        } else
        {
            //do nothing
        }
    }

    private void initWeeklyForecast()
    {
        if (mForecastDetails != null)
        {
            HashMap<Integer, Temperature> weeklyForecastList = mForecastDetails.getTemperatureForecast();
            Set<Integer> keys = weeklyForecastList.keySet();
            if (!keys.isEmpty())
            {
                fiveDayForecast.removeAllViews();
                for (Integer dayKey : keys)
                {
                    View weeklyForecastView = getActivity().getLayoutInflater().inflate(R.layout.daily_forecast_item, null, false);
                    TextView day = (TextView) weeklyForecastView.findViewById(R.id.day);
                    TextView minTemp = (TextView) weeklyForecastView.findViewById(R.id.min_temp);
                    TextView maxTemp = (TextView) weeklyForecastView.findViewById(R.id.max_temp);
                    Temperature temperature = weeklyForecastList.get(dayKey);
                    String setDay = UtilityClass.getDayOfWeek(dayKey) + Constants.EMPTY;
                    day.setText(setDay);
                    minTemp.setText(UtilityClass.getTemp(temperature.getMin(), mTempType));
                    maxTemp.setText(UtilityClass.getTemp(temperature.getMax(), mTempType));
                    fiveDayForecast.addView(weeklyForecastView);
                }
            }
        }
    }

    public void showForecast(@NonNull ForecastDetails forecastDetails)
    {
        mForecastDetails = forecastDetails;
        ArrayList<WeatherDetails> forecastList = mForecastDetails.getWeatherDetails();
        if (forecastList != null && !forecastList.isEmpty())
        {
            mCurrentWeatherDetails = forecastList.get(0);
            initCurrentWeather();
            initHourlyForecast();
            initWeeklyForecast();
        }
    }

    @OnClick(R.id.current_weather_container)
    void onClickCurrentWeather()
    {
        if (mCurrentWeatherDetails != null)
        {
            mCallback.showCurrentWeatherDetails(mCurrentWeatherDetails);
        }
    }

    @Override
    public void showWeatherForecast(ForecastDetails forecastDetails)
    {
        showForecast(forecastDetails);
    }

    public interface Callbacks
    {
        void showCurrentWeatherDetails(WeatherDetails currentWeatherDetails);
    }
}
