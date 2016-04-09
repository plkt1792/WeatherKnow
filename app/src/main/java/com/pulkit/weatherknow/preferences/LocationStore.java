package com.pulkit.weatherknow.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.pulkit.weatherknow.BaseApplication;
import com.pulkit.weatherknow.utils.Constants;

/**
 * @author pulkit
 */
public class LocationStore
{
    private final static String LOCATION_PREF = "location_pref";
    private final static String LATITUDE = "latitude";
    private final static String LONGITUDE = "longitude";
    private final static String CITY = "city";

    private static SharedPreferences getLocationPreferences()
    {
        return BaseApplication.getContext().getSharedPreferences(LOCATION_PREF, Context.MODE_PRIVATE);
    }

    public static void setCity(String city)
    {
        SharedPreferences.Editor editor = getLocationPreferences().edit();
        editor.putString(CITY, city);
        editor.apply();
    }

    public static String getCity()
    {
        return getLocationPreferences().getString(CITY, Constants.EMPTY);
    }

    public static void setLatitude(String latitude)
    {
        SharedPreferences.Editor editor = getLocationPreferences().edit();
        editor.putString(LATITUDE, latitude);
        editor.apply();
    }

    public static String getLatitude()
    {
        return getLocationPreferences().getString(LATITUDE, Constants.EMPTY);
    }

    public static void setLongitude(String longitude)
    {
        SharedPreferences.Editor editor = getLocationPreferences().edit();
        editor.putString(LONGITUDE, longitude);
        editor.apply();
    }

    public static String getLongitude()
    {
        return getLocationPreferences().getString(LONGITUDE, Constants.EMPTY);
    }

    public static void clearLocationDetails()
    {
        setLatitude(Constants.EMPTY);
        setLongitude(Constants.EMPTY);
    }

    public static boolean isLocationAvailable()
    {
        return !TextUtils.isEmpty(getLatitude()) && !TextUtils.isEmpty(getLongitude());
    }
}
