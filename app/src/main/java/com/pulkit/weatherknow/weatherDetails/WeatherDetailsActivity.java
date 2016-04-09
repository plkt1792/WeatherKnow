package com.pulkit.weatherknow.weatherDetails;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pulkit.weatherknow.R;
import com.pulkit.weatherknow.entities.ForecastDetails;
import com.pulkit.weatherknow.entities.WeatherDetails;
import com.pulkit.weatherknow.preferences.ImageDownloadStore;
import com.pulkit.weatherknow.preferences.LocationStore;
import com.pulkit.weatherknow.utils.Constants;
import com.pulkit.weatherknow.utils.UtilityClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WeatherDetailsActivity extends AppCompatActivity implements IWeatherForecastView, WeatherForecastFragment.Callbacks
{
    private static final String DATE_TIME_FORMAT = "dd/MM/yyyy hh:mm a";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String WEATHER_FORECAST_FRAGMENT = "weather_forecast_fragment";
    private static final String CURRENT_WEATHER_FRAGMENT = "fragment_current_weather";
    private static final String LOADING_TXT = "Loading...";
    private static final String ERROR = "Error in getting weather details";
    public static final String FORECAST_DETAILS = "forecast_details";
    public static final String CITY = "city";

    private IWeatherActivityCallback activityCallback;
    private ForecastDetails mForecastDetails;
    private IWeatherDetailsPresenter presenter;
    private ProgressDialog progressDialog;
    private Toast toast;

    @Bind(R.id.background_image)
    ImageView backgroundImage;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.app_bar_title)
    TextView toolbarTitle;
    @Bind(R.id.app_bar_sub_title)
    TextView toolbarSubTitle;

    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equals(Constants.LOAD_NEW_BG_IMAGE))
            {
                setBackground();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);
        ButterKnife.bind(this);
        getIntentExtras();
        if (savedInstanceState != null)
        {
            mForecastDetails = (ForecastDetails) savedInstanceState.getSerializable(FORECAST_DETAILS);
        }
        setWeatherForecastFragment();
        initToolbar();
        presenter = new WeatherDetailsPresenter(this);
        presenter.getWeatherForecast();
        UtilityClass.setDownloadImageAlarm();
        setBackground();
    }

    private void getIntentExtras()
    {
        Intent intent = getIntent();
        if (intent != null)
        {
            String action = intent.getAction();
            Uri data = intent.getData();
            if (Intent.ACTION_VIEW.equals(action) && data != null)
            {
                String latitude = data.getQueryParameter(LATITUDE);
                String longitude = data.getQueryParameter(LONGITUDE);
                String city = data.getQueryParameter(CITY);
                if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude))
                {
                    LocationStore.setLongitude(longitude);
                    LocationStore.setLatitude(latitude);
                }
                if (!TextUtils.isEmpty(city))
                {
                    LocationStore.setCity(city);
                    LocationStore.clearLocationDetails();
                }
            }
        }
    }

    private void setBackground()
    {
        String url = ImageDownloadStore.getImageUrl();
        Glide.with(this).load(url).asBitmap().centerCrop().into(backgroundImage);
    }

    private void initToolbar()
    {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.LOAD_NEW_BG_IMAGE);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onStop()
    {
        unregisterReceiver(receiver);
        super.onStop();
    }

    @Override
    public void onBackPressed()
    {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(CURRENT_WEATHER_FRAGMENT);
        if (fragment != null)
        {
            setWeatherForecastFragment();
        } else
        {
            super.onBackPressed();
        }
    }

    public void setToolbar()
    {
        toolbarTitle.setText(mForecastDetails.getCity());
        DateFormat dateFormatter = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.US);
        dateFormatter.setLenient(false);
        Date today = new Date();
        String currentDateTime = dateFormatter.format(today);
        toolbarSubTitle.setText(currentDateTime);
    }

    @Override
    public void showCurrentWeatherDetails(WeatherDetails currentWeatherDetails)
    {
        Fragment currentWeatherFragment = CurrentWeatherDetailsFragment.newInstance(currentWeatherDetails);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, currentWeatherFragment, CURRENT_WEATHER_FRAGMENT);
        transaction.commit();
    }

    private void setWeatherForecastFragment()
    {
        Fragment weatherForecastFragment = WeatherForecastFragment.newInstance(mForecastDetails);
        activityCallback = (IWeatherActivityCallback) weatherForecastFragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, weatherForecastFragment, WEATHER_FORECAST_FRAGMENT);
        transaction.commit();
    }


    @Override
    public void onDestroy()
    {
        if (progressDialog != null)
        {
            if (progressDialog.isShowing())
            {
                progressDialog.cancel();
            }
            progressDialog = null;
        }
        if (toast != null)
        {
            toast.cancel();
        }
        presenter.cancelAllTasks();
        super.onDestroy();
    }


    private void initProgressBar()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(LOADING_TXT);
        progressDialog.setCancelable(true);
    }

    @Override
    public void showProgress()
    {
        if (progressDialog != null)
        {
            progressDialog.show();
        } else
        {
            initProgressBar();
            progressDialog.show();
        }
    }

    @Override
    public void hideProgress()
    {
        if (progressDialog != null)
        {
            if (progressDialog.isShowing())
            {
                progressDialog.cancel();
            }
        }
    }

    @Override
    public void showError()
    {
        toast = Toast.makeText(this, ERROR, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void showForecast(@NonNull ForecastDetails forecastDetails)
    {
        mForecastDetails = forecastDetails;
        ArrayList<WeatherDetails> forecastList = mForecastDetails.getWeatherDetails();
        if (forecastList != null && !forecastList.isEmpty())
        {
            if (activityCallback != null)
            {
                activityCallback.showWeatherForecast(mForecastDetails);
            }
            setToolbar();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putSerializable(FORECAST_DETAILS, mForecastDetails);
        super.onSaveInstanceState(outState);
    }
}
