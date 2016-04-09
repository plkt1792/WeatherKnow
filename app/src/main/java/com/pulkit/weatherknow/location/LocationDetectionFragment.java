package com.pulkit.weatherknow.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.pulkit.weatherknow.R;
import com.pulkit.weatherknow.utils.UtilityClass;

/**
 * @author pulkit
 */

/**
 * A headless fragment which handles location detection mechanism. It manages run time permissions and location settings.
 * Redirect onActivityResult() for {@link #REQUEST_CODE_CHECK_SETTINGS} to this fragment.
 */
public class LocationDetectionFragment extends Fragment implements LocationDetector.LocationDetectionListener
{
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 123;
    public static final int REQUEST_CODE_CHECK_SETTINGS = 321;
    private Toast toast;
    private LocationCallback locationCallback;
    private LocationDetector locationDetector;

    public LocationDetectionFragment()
    {
    }

    public static LocationDetectionFragment newInstance()
    {
        return new LocationDetectionFragment();
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        locationCallback = UtilityClass.getParent(this, LocationCallback.class);
        if (locationCallback == null)
        {
            throw new RuntimeException(new StringBuilder(5).append(context.getClass().getSimpleName()).append(" must implement ").append(LocationCallback.class.getSimpleName()).toString());
        } else
        {
            detectLocation();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        stopLocationDetection();
        hidePreviousMessage();
    }

    @Override
    public void onLocationPermissionMissing()
    {
        requestPermission();
    }

    @Override
    public void onLocationDetected(Location location)
    {
        locationDetector.stopLocationDetection();
        locationCallback.onLocationDetected(location);
    }

    @Override
    public void onLocationDetectionFailed(String reason)
    {
        locationDetector.stopLocationDetection();
        locationCallback.onLocationDetectionFailed();
    }

    @Override
    public void onLocationSettingsResolutionRequired(Status status)
    {
        try
        {
            status.startResolutionForResult(getActivity(), REQUEST_CODE_CHECK_SETTINGS);
        } catch (IntentSender.SendIntentException ignored)
        {
            // do nothing
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case REQUEST_CODE_LOCATION_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    detectLocation();
                } else
                {
                    locationCallback.onLocationPermissionDenied();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case REQUEST_CODE_CHECK_SETTINGS:
                switch (resultCode)
                {
                    case Activity.RESULT_OK:
                        detectLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        locationCallback.onLocationSettingsFailed();
                        break;
                    default:
                        locationCallback.onLocationSettingsFailed();
                        break;
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void detectLocation()
    {
        if (locationDetector == null)
        {
            locationDetector = new LocationDetector(this);
            locationDetector.detectLocation();
        } else
        {
            locationDetector.detectLocation();
        }
    }

    private void stopLocationDetection()
    {
        if (locationDetector != null)
        {
            locationDetector.stopLocationDetection();
            locationDetector = null;
        } else
        {
            // do nothing
        }
    }

    private void requestPermission()
    {
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION))
        {
            showMessage(getResources().getString(R.string.location_permission_message), Toast.LENGTH_LONG);
        } else
        {
            // do not show rationale
        }
        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
    }

    private void hidePreviousMessage()
    {
        if (toast != null)
        {
            toast.cancel();
        } else
        {
            // do nothing
        }
    }

    private void showMessage(CharSequence message, int length)
    {
        hidePreviousMessage();
        toast = Toast.makeText(getContext(), message, length);
        toast.show();
    }

    public interface LocationCallback
    {
        void onLocationDetected(Location location);

        void onLocationPermissionDenied();

        void onLocationSettingsFailed();

        void onLocationDetectionFailed();
    }
}