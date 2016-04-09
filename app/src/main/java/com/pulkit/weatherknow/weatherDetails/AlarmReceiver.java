package com.pulkit.weatherknow.weatherDetails;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pulkit.weatherknow.BaseApplication;
import com.pulkit.weatherknow.utils.Constants;

public class AlarmReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Constants.DOWNLOAD_IMAGE))
        {
            Intent i = new Intent(Constants.LOAD_NEW_BG_IMAGE);
            BaseApplication.getContext().sendBroadcast(i);
        }
    }
}
