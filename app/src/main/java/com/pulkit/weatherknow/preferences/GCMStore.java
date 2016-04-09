package com.pulkit.weatherknow.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.pulkit.weatherknow.BaseApplication;
import com.pulkit.weatherknow.utils.Constants;

/**
 * @author pulkit
 */
public class GCMStore
{
    private static final String GCM_PREF = "gcm_pref";
    private static final String REGISTRATION_TOKEN = "gcm_token";

    private static SharedPreferences getGCMPreferences()
    {
        return BaseApplication.getContext().getSharedPreferences(GCM_PREF, Context.MODE_PRIVATE);
    }

    public static void setRegistrationToken(String token)
    {
        Editor editor = getGCMPreferences().edit();
        editor.putString(REGISTRATION_TOKEN, token);
        editor.apply();
    }

    public static String getRegistrationToken()
    {
        return getGCMPreferences().getString(REGISTRATION_TOKEN, Constants.EMPTY);
    }

}
