package com.pulkit.weatherknow.gcm;

import android.app.IntentService;
import android.content.Intent;

/**
 * @author pulkit
 */
public class GCMIntentService extends IntentService
{

    public GCMIntentService()
    {
        super(GCMIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Utils.generateGCMToken();
    }
}
