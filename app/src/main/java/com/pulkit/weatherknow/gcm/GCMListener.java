package com.pulkit.weatherknow.gcm;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * @author pulkit
 */
public class GCMListener extends GcmListenerService
{
    @Override
    public void onMessageReceived(String from, final Bundle data)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Utils.showNotification(data);
            }
        }).start();
        super.onMessageReceived(from, data);
    }

    @Override
    public void onMessageSent(String msgId)
    {
        super.onMessageSent(msgId);
    }

    @Override
    public void onSendError(String msgId, String error)
    {
        super.onSendError(msgId, error);
    }
}
