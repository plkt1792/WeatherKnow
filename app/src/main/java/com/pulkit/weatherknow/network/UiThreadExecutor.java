package com.pulkit.weatherknow.network;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

/**
 * @author pulkit
 */
public class UiThreadExecutor implements Executor
{
    @Override
    public void execute(@NonNull Runnable runnable)
    {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
