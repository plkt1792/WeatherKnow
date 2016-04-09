package com.pulkit.weatherknow.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pulkit.weatherknow.BaseApplication;
import com.pulkit.weatherknow.R;
import com.pulkit.weatherknow.entities.TempType;
import com.pulkit.weatherknow.entities.Temperature;
import com.pulkit.weatherknow.entities.WeatherDetails;
import com.pulkit.weatherknow.preferences.LocationStore;
import com.pulkit.weatherknow.splashScreen.ISplashPresenter;
import com.pulkit.weatherknow.weatherDetails.AlarmReceiver;
import com.pulkit.weatherknow.weatherDetails.IWeatherDetailsPresenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * @author pulkit
 */
public class UtilityClass
{
    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    private static final Context sContext = BaseApplication.getContext();
    public static final String PLACE_NAME_CAN_T_BE_LEFT_BLANK = "Place name can't be left blank";

    public static <T> T getParent(@NonNull Fragment fragment, @NonNull Class<T> parentClass)
    {
        Fragment parentFragment = fragment.getParentFragment();
        if (parentClass.isInstance(parentFragment))
        {
            return (T) parentFragment;
        } else if (parentClass.isInstance(fragment.getActivity()))
        {
            return (T) fragment.getActivity();
        }
        return null;
    }

    public static void showErrorDialog(@NonNull final Activity activity, @NonNull final String message, final ISplashPresenter presenter)
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = activity.getLayoutInflater();
        View v = inflater.inflate(R.layout.layout_alert_dialog, null);
        ((TextView) v.findViewById(R.id.txt)).setText(message);
        final EditText city = (EditText) v.findViewById(R.id.city_name);
        v.findViewById(R.id.btnOkay).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final String cityName = city.getText().toString();
                if (!TextUtils.isEmpty(cityName))
                {
                    LocationStore.setCity(cityName);
                    dialog.dismiss();
                    if (presenter != null)
                    {
                        presenter.showNextActivity();
                    }
                } else
                {
                    Toast.makeText(sContext, PLACE_NAME_CAN_T_BE_LEFT_BLANK, Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.setContentView(v);
        try
        {
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
        } catch (Exception e)
        {
            //don't show the dialog
        }
    }

    public static String getCurrentLatLong()
    {
        if (LocationStore.isLocationAvailable())
        {
            StringBuilder builder = new StringBuilder();
            builder.append(LocationStore.getLatitude()).append(Constants.COMMA_DELIMITER).append(LocationStore.getLongitude());
            return builder.toString();
        } else
        {
            return Constants.EMPTY;
        }
    }

    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static double kelvinToCelsius(double tempInKelvin)
    {
        return (tempInKelvin - Constants.KELVIN_CONSTANT);
    }

    public static double kelvinToFahrenheit(double tempInKelvin)
    {
        return 9 * (kelvinToCelsius(tempInKelvin)) / 5 + 32;
    }

    public static String getDayOfWeek(Integer day)
    {
        switch (day)
        {
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
            default:
                return null;
        }
    }

    public static String getTemp(double temp, TempType type)
    {
        switch (type)
        {
            case K:
                return String.format(sContext.getResources().getString(R.string.temp_txt), temp) + type.name();
            case C:
                return String.format(sContext.getResources().getString(R.string.temp_txt), UtilityClass.kelvinToCelsius(temp)) + type.name();
            case F:
                return String.format(sContext.getResources().getString(R.string.temp_txt), UtilityClass.kelvinToFahrenheit(temp)) + type.name();
            default:
                return String.format(sContext.getResources().getString(R.string.temp_txt), temp) + type.name();
        }
    }

    public static HashMap<Integer, Temperature> getWeeklyForecast(ArrayList<WeatherDetails> forecastList)
    {
        Calendar day = Calendar.getInstance();
        HashMap<Integer, Temperature> weekForecast = new HashMap<>();
        for (WeatherDetails weatherDetails : forecastList)
        {
            Date date = getDateFromDateTxt(weatherDetails.getDateTxt());
            if (date != null)
            {
                day.setTime(date);
                int dayKey = day.get(Calendar.DAY_OF_WEEK);
                if (weekForecast.containsKey(dayKey))
                {
                    weekForecast.put(dayKey, getBetterResult(getTempFromWeather(weatherDetails), weekForecast.get(dayKey)));
                } else
                {
                    weekForecast.put(dayKey, getTempFromWeather(weatherDetails));
                }
            } else
            {
                //do nothing
            }
        }
        return weekForecast;
    }

    private static Temperature getBetterResult(Temperature t1, Temperature t2)
    {
        Temperature t = new Temperature();
        t.setMax(t1.getMax() > t2.getMax() ? t1.getMax() : t2.getMax());
        t.setMin(t1.getMin() < t2.getMin() ? t1.getMin() : t2.getMin());
        t.setFeelsLike((t.getMax() + t.getMin()) / 2);
        return t;
    }

    private static Temperature getTempFromWeather(WeatherDetails weatherDetails)
    {
        Temperature t = new Temperature();
        t.setMax(weatherDetails.getMaxTemp());
        t.setMin(weatherDetails.getMinTemp());
        t.setFeelsLike(weatherDetails.getCurrentTemp());
        return t;
    }

    public static Date getDateFromDateTxt(String dateTxt)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS, Locale.US);
        Date date = null;
        try
        {
            date = sdf.parse(dateTxt);
            return date;
        } catch (ParseException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static void setDownloadImageAlarm()
    {
        Intent alarmIntent = new Intent(sContext, AlarmReceiver.class);
        alarmIntent.setAction(Constants.DOWNLOAD_IMAGE);
        Calendar calendar = Calendar.getInstance();
        int request = (int) calendar.getTimeInMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(sContext, request, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) sContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HALF_HOUR, pendingIntent);
    }
}
