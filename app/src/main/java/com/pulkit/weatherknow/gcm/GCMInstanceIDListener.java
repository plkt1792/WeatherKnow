package com.pulkit.weatherknow.gcm;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * @author pulkit
 */
public class GCMInstanceIDListener extends InstanceIDListenerService
{
    @Override
    public void onTokenRefresh()
    {
        Intent intent = new Intent(this, GCMIntentService.class);
        startService(intent);
        super.onTokenRefresh();
    }
}
