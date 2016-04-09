package com.pulkit.weatherknow.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.pulkit.weatherknow.BaseApplication;

/**
 * @author pulkit
 */
public class LocationDetector implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, ResultCallback<LocationSettingsResult>
{
    private static final long LOCATION_UPDATES_INTERVAL_IN_MILLIS = 10 * 1000;
    private static final long LOCATION_FASTEST_UPDATE_INTERVAL_IN_MILLIS = 500;
    private static final String FAILURE_CONNECTION_SUSPENDED = "Connection suspended.";
    private static final String FAILURE_CONNECTION_FAILED = "Failed to connect location API.";
    private static final String FAILURE_SETTINGS_CHANGE_UNAVAILABLE = "Location settings can't be changed to meet the requirements.";
    private LocationDetectionListener locationDetectionListener;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationSettingsRequest.Builder locationSettingsRequestBuilder;
    private boolean detectingLocation;


    private LocationDetector()
    {

    }

    public LocationDetector(@NonNull LocationDetectionListener locationDetectionListener)
    {
        this.locationDetectionListener = locationDetectionListener;

        googleApiClient = new GoogleApiClient.Builder(BaseApplication.getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(LOCATION_UPDATES_INTERVAL_IN_MILLIS)
                .setFastestInterval(LOCATION_FASTEST_UPDATE_INTERVAL_IN_MILLIS);

        locationSettingsRequestBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true);

        detectingLocation = false;
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        requestLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        stopLocationDetection();
        locationDetectionListener.onLocationDetectionFailed(FAILURE_CONNECTION_SUSPENDED);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        stopLocationDetection();
        locationDetectionListener.onLocationDetectionFailed(FAILURE_CONNECTION_FAILED);
    }

    @Override
    public void onLocationChanged(Location location)
    {
        detectingLocation = false;
        locationDetectionListener.onLocationDetected(location);
    }

    @Override
    public void onResult(LocationSettingsResult locationSettingsResult)
    {
        Status status = locationSettingsResult.getStatus();

        switch (status.getStatusCode())
        {
            case LocationSettingsStatusCodes.SUCCESS:
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                detectingLocation = false;
                locationDetectionListener.onLocationSettingsResolutionRequired(status);
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                stopLocationDetection();
                locationDetectionListener.onLocationDetectionFailed(FAILURE_SETTINGS_CHANGE_UNAVAILABLE);
                break;
            default:
                // do nothing
                break;
        }
    }

    private void checkLocationSettings()
    {
        PendingResult<LocationSettingsResult> locationSettingsResult = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, locationSettingsRequestBuilder.build());
        locationSettingsResult.setResultCallback(this);
    }

    private void requestLocationUpdates()
    {
        if (ContextCompat.checkSelfPermission(BaseApplication.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            locationDetectionListener.onLocationPermissionMissing();
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    /**
     * Detects current location using {@link GoogleApiClient} and {@link LocationServices#FusedLocationApi}.
     * <code>LocationDetector<code/> takes care of redundancy. If a previous request is still running, any
     * successive calls will be ignored. Location detection requires <code>ACCESS_COARSE_LOCATION<code/> permission.
     * If location permission is missing, caller will receive {@link LocationDetectionListener#onLocationPermissionMissing()}
     * callback. In such a case, caller should gain required permission and call this method again.
     */
    public void detectLocation()
    {
        if (!detectingLocation)
        {
            if (hasLocationPermission())
            {
                detectingLocation = true;
                connectToLocationAPI();
                checkLocationSettings();
            } else
            {
                locationDetectionListener.onLocationPermissionMissing();
            }
        } else
        {
            // do nothing
        }
    }

    private boolean hasLocationPermission()
    {
        int locationPermission = ContextCompat.checkSelfPermission(BaseApplication.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        return PackageManager.PERMISSION_GRANTED == locationPermission;
    }

    private void connectToLocationAPI()
    {
        if (googleApiClient.isConnected() || googleApiClient.isConnecting())
        {
            // do nothing, already connected (or connecting)
        } else
        {
            googleApiClient.connect();
        }
    }

    /**
     * Stop previous request (if any) to detect location. This will stop all location updates and disconnect
     * {@link GoogleApiClient}.
     */
    public void stopLocationDetection()
    {
        detectingLocation = false;
        if (googleApiClient.isConnected())
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        } else
        {
            // do nothing
        }
    }

    public interface LocationDetectionListener
    {
        void onLocationPermissionMissing();

        void onLocationDetected(Location location);

        void onLocationDetectionFailed(String reason);

        void onLocationSettingsResolutionRequired(Status status);
    }
}

