package com.pulkit.weatherknow.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.Html;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.pulkit.weatherknow.BaseApplication;
import com.pulkit.weatherknow.R;
import com.pulkit.weatherknow.preferences.GCMStore;
import com.pulkit.weatherknow.splashScreen.SplashScreen;
import com.pulkit.weatherknow.utils.Constants;
import com.pulkit.weatherknow.weatherDetails.WeatherDetailsActivity;

import java.util.Random;

/**
 * @author pulkit
 */
public class Utils
{
    private static final Context sContext = BaseApplication.getContext();
    private static int notificationId;

    public static void generateGCMToken()
    {
        InstanceID instanceID = InstanceID.getInstance(BaseApplication.getContext());
        try
        {
            String token = instanceID.getToken(GCMConstants.SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            GCMStore.setRegistrationToken(token);
        } catch (Exception e)
        {
            e.printStackTrace();
            GCMStore.setRegistrationToken(Constants.EMPTY);
        }
    }

    public static void showNotification(Bundle extras)
    {
        notificationId = generateNotificationId();
        NotificationCompat.Builder notificationBuilder = buildNotification(extras);
        addAction(notificationBuilder);

        NotificationManager notificationManager = (NotificationManager) sContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    private static NotificationCompat.Builder buildNotification(Bundle extras)
    {
        Intent resultIntent = new Intent(sContext, SplashScreen.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        resultIntent.putExtras(extras);
        PendingIntent pendingIntent = PendingIntent.getActivity(sContext, generateRandomInt(), resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Builder(sContext)
                .setSmallIcon(R.drawable.ic_cloud)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(extras.getString("title"))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(extras.getString("message")))
                .setContentText(Html.fromHtml(extras.getString("message")))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
    }

    private static void addAction(NotificationCompat.Builder builder)
    {
        Intent intent = new Intent(sContext, WeatherDetailsActivity.class);
        intent.putExtra("Notification_id", notificationId);
        PendingIntent pendingIntent = PendingIntent.getActivity(sContext, generateRandomInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(android.R.drawable.ic_notification_clear_all, "start", pendingIntent);
    }

    private static int generateRandomInt()
    {
        Random random = new Random();
        int Low = 10;
        int High = 100000;
        return random.nextInt(High - Low) + Low;
    }

    private static int generateNotificationId()
    {
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        return rand.nextInt(Integer.MAX_VALUE);
    }

}


