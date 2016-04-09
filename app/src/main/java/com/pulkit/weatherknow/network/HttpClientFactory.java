package com.pulkit.weatherknow.network;


import com.pulkit.weatherknow.BaseApplication;

import net.jcip.annotations.GuardedBy;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author pulkit
 */
public class HttpClientFactory
{
    private static final Object LOCK = new Object();
    public static final int READ_TIMEOUT_IN_MS = 30000;
    public static final int WRITE_TIMEOUT_IN_MS = 30000;
    public static final int CONNECT_TIMEOUT_IN_MS = 30000;

    @GuardedBy("LOCK")
    private static OkHttpClient mOkHttpClient;

    public static OkHttpClient getClient()
    {
        synchronized (LOCK)
        {
            if (mOkHttpClient == null)
            {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                mOkHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(CONNECT_TIMEOUT_IN_MS, TimeUnit.MILLISECONDS)
                        .readTimeout(READ_TIMEOUT_IN_MS, TimeUnit.MILLISECONDS)
                        .writeTimeout(WRITE_TIMEOUT_IN_MS, TimeUnit.MILLISECONDS)
                        .cache(addCacheDirectory())
                        .addInterceptor(logging)
                        .build();
            }
        }
        return mOkHttpClient;
    }

    private static Cache addCacheDirectory()
    {
        final int cacheSize = 5 * 1024 * 1024; // 5 MB
        File cacheDir = BaseApplication.getContext().getCacheDir();
        return new Cache(cacheDir, cacheSize);
    }
}
