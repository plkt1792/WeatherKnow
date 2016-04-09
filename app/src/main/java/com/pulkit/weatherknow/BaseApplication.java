package com.pulkit.weatherknow;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.pulkit.weatherknow.gcm.GCMIntentService;

/**
 * @author pulkit
 */
public class BaseApplication extends Application
{
    private static Context context;

    public static Context getContext()
    {
        return context;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        context = this;
    }
}
