package com.pulkit.weatherknow.splashScreen;

import android.os.Handler;

/**
 * @author pulkit
 */
public class SplashScreenPresenter implements ISplashPresenter
{
    private static final int DELAY_MILLIS = 2000;
    private ISplashView splashView;

    public SplashScreenPresenter(ISplashView splashView)
    {
        this.splashView = splashView;
    }


    @Override
    public void showNextActivity()
    {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                splashView.startNextActivity();
            }
        }, DELAY_MILLIS);
    }
}
