package com.pulkit.weatherknow.weatherDetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pulkit.weatherknow.R;
import com.pulkit.weatherknow.entities.TempType;
import com.pulkit.weatherknow.entities.WeatherDetails;
import com.pulkit.weatherknow.preferences.ImageDownloadStore;
import com.pulkit.weatherknow.utils.Constants;
import com.pulkit.weatherknow.utils.UtilityClass;

/**
 * @author pulkit
 */
public class CurrentWeatherDetailsFragment extends Fragment
{
    public static final String FEELS_LIKE = "Feels like";
    public static final String HUMIDITY = "Humidity %";
    public static final String PERCENT = " %";
    public static final String CLOUDS = "Clouds %";
    public static final String MM = "mm";
    public static final String RAINFALL_IN_3_HOURS = "Rainfall (in 3 hours)";
    public static final String NO_RECENT_RECORDS = "N.A.";
    public static final String WIND_SPEED_DIRECTION = "Wind (speed/direction)";
    public static final String M_S = " m/s";
    public static final String H_PA = " hPa";
    public static final String PRESSURE = "Pressure";
    public static final String SEA_LEVEL_PRESSURE = "Sea Level Pressure";
    public static final String GROUND_LEVEL_PRESSURE = "Ground Level Pressure";
    private WeatherDetails currentWeather;

    public static CurrentWeatherDetailsFragment newInstance(WeatherDetails weatherDetails)
    {
        CurrentWeatherDetailsFragment fragment = new CurrentWeatherDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.CURRENT_WEATHER_DETAILS, weatherDetails);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        currentWeather = (WeatherDetails) getArguments().getSerializable(Constants.CURRENT_WEATHER_DETAILS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_current_weather, container, false);
        initFeelsLikeTemp(view);
        initHumidity(view);
        initCloud(view);
        initRain(view);
        initWind(view);
        initPressure(view);
        initSeaLevel(view);
        initGrndLevel(view);
        return view;

    }

    private void initFeelsLikeTemp(View view)
    {
        View tempView = view.findViewById(R.id.feels_like_temp);
        ((TextView) tempView.findViewById(R.id.weather_param)).setText(FEELS_LIKE);
        ((TextView) tempView.findViewById(R.id.details)).setText(UtilityClass.getTemp(currentWeather.getCurrentTemp(), TempType.C));
        tempView.findViewById(R.id.underline).setVisibility(View.GONE);
    }

    private void initHumidity(View view)
    {
        View humidityView = view.findViewById(R.id.humidity);
        if (currentWeather.getHumidityPercentage() == -1)
        {
            humidityView.setVisibility(View.GONE);
        } else
        {
            humidityView.setVisibility(View.VISIBLE);
            ((TextView) humidityView.findViewById(R.id.weather_param)).setText(HUMIDITY);
            ((TextView) humidityView.findViewById(R.id.details)).setText(currentWeather.getHumidityPercentage() + PERCENT);
            humidityView.findViewById(R.id.underline).setVisibility(View.GONE);
        }
    }

    private void initCloud(View view)
    {
        View cloudView = view.findViewById(R.id.cloud);
        if (currentWeather.getCloudPercentage() == -1)
        {
            cloudView.setVisibility(View.GONE);
        } else
        {
            cloudView.setVisibility(View.VISIBLE);
            ((TextView) cloudView.findViewById(R.id.weather_param)).setText(CLOUDS);
            ((TextView) cloudView.findViewById(R.id.details)).setText(currentWeather.getCloudPercentage() + PERCENT);
            cloudView.findViewById(R.id.underline).setVisibility(View.GONE);
        }
    }

    private void initRain(View view)
    {
        View rainView = view.findViewById(R.id.rainfall);
        rainView.setVisibility(View.VISIBLE);
        ((TextView) rainView.findViewById(R.id.weather_param)).setText(RAINFALL_IN_3_HOURS);
        if (currentWeather.getRainIn3Hours() != -1)
        {
            ((TextView) rainView.findViewById(R.id.details)).setText(currentWeather.getRainIn3Hours() + MM);
        } else
        {
            ((TextView) rainView.findViewById(R.id.details)).setText(NO_RECENT_RECORDS);
        }
        rainView.findViewById(R.id.underline).setVisibility(View.GONE);
    }


    private void initWind(View view)
    {
        View windView = view.findViewById(R.id.wind);
        if (currentWeather.getWindSpeed() != -1 && currentWeather.getWindDirectionDegrees() != -1)
        {
            windView.setVisibility(View.VISIBLE);
            ((TextView) windView.findViewById(R.id.day)).setText(WIND_SPEED_DIRECTION);
            ((TextView) windView.findViewById(R.id.min_temp)).setText(currentWeather.getWindSpeed() + M_S);
            ((TextView) windView.findViewById(R.id.max_temp)).setText(currentWeather.getCloudPercentage() + getContext().getResources().getString(R.string.degree));
            windView.findViewById(R.id.underline).setVisibility(View.GONE);
        } else
        {
            windView.setVisibility(View.GONE);
        }
    }

    private void initPressure(View view)
    {
        View pressureView = view.findViewById(R.id.pressure);
        if (currentWeather.getPressure() == -1)
        {
            pressureView.setVisibility(View.GONE);
        } else
        {
            pressureView.setVisibility(View.VISIBLE);
            ((TextView) pressureView.findViewById(R.id.weather_param)).setText(PRESSURE);
            ((TextView) pressureView.findViewById(R.id.details)).setText(currentWeather.getPressure() + H_PA);
            pressureView.findViewById(R.id.underline).setVisibility(View.GONE);
        }
    }

    private void initSeaLevel(View view)
    {
        View v = view.findViewById(R.id.sea_level);
        if (currentWeather.getSeaLevel() == -1)
        {
            v.setVisibility(View.GONE);
        } else
        {
            v.setVisibility(View.VISIBLE);
            ((TextView) v.findViewById(R.id.weather_param)).setText(SEA_LEVEL_PRESSURE);
            ((TextView) v.findViewById(R.id.details)).setText(currentWeather.getSeaLevel() + H_PA);
            v.findViewById(R.id.underline).setVisibility(View.GONE);
        }
    }

    private void initGrndLevel(View view)
    {
        View v = view.findViewById(R.id.grnd_level);
        if (currentWeather.getGroundLevel() == -1)
        {
            v.setVisibility(View.GONE);
        } else
        {
            v.setVisibility(View.VISIBLE);
            ((TextView) v.findViewById(R.id.weather_param)).setText(GROUND_LEVEL_PRESSURE);
            ((TextView) v.findViewById(R.id.details)).setText(currentWeather.getPressure() + H_PA);
            v.findViewById(R.id.underline).setVisibility(View.GONE);
        }
    }
}
