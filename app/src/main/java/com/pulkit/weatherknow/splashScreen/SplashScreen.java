package com.pulkit.weatherknow.splashScreen;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pulkit.weatherknow.R;
import com.pulkit.weatherknow.gcm.GCMIntentService;
import com.pulkit.weatherknow.location.LocationDetectionFragment;
import com.pulkit.weatherknow.preferences.LocationStore;
import com.pulkit.weatherknow.utils.Constants;
import com.pulkit.weatherknow.utils.UtilityClass;
import com.pulkit.weatherknow.weatherDetails.WeatherDetailsActivity;

public class SplashScreen extends AppCompatActivity implements ISplashView, LocationDetectionFragment.LocationCallback
{
    private static final String TAG_LOCATION_HELPER_FRAGMENT = "TAG_LOCATION_HELPER_FRAGMENT";
    private ISplashPresenter presenter;
    private LocationDetectionFragment locationDetectionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getGCMToken();
        presenter = new SplashScreenPresenter(this);
        autoDetectLocation();
    }

    private void getGCMToken()
    {
        Intent intent = new Intent(this, GCMIntentService.class);
        startService(intent);
    }

    @Override
    public void startNextActivity()
    {
        Intent intent = new Intent(this, WeatherDetailsActivity.class);
        startActivity(intent);
        finish();
    }

    private void autoDetectLocation()
    {
        locationDetectionFragment = LocationDetectionFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(locationDetectionFragment, TAG_LOCATION_HELPER_FRAGMENT).commit();
    }

    @Override
    protected void onDestroy()
    {
        locationDetectionFragment = null;
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LocationDetectionFragment.REQUEST_CODE_CHECK_SETTINGS)
        {
            if (locationDetectionFragment != null)
            {
                locationDetectionFragment.onActivityResult(requestCode, resultCode, data);
            } else
            {
                onLocationSettingsFailed();
            }
        } else
        {
            // do nothing
        }
    }

    @Override
    public void onLocationDetected(Location location)
    {
        if (location != null)
        {
            LocationStore.setLatitude(String.valueOf(location.getLatitude()));
            LocationStore.setLongitude(String.valueOf(location.getLongitude()));
            presenter.showNextActivity();
        } else
        {
            onCurrentLocationFetchFailed();
        }
    }

    @Override
    public void onLocationPermissionDenied()
    {
        onCurrentLocationFetchFailed();
    }

    @Override
    public void onLocationSettingsFailed()
    {
        onCurrentLocationFetchFailed();
    }

    @Override
    public void onLocationDetectionFailed()
    {
        onCurrentLocationFetchFailed();
    }

    private void onCurrentLocationFetchFailed()
    {
        if (!LocationStore.isLocationAvailable())
        {
            UtilityClass.showErrorDialog(this, Constants.LOCATION_NOT_DETECTED_MSG, presenter);
        } else
        {
            presenter.showNextActivity();
        }
    }
}
