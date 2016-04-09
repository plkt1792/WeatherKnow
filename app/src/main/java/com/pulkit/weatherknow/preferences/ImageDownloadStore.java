package com.pulkit.weatherknow.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.pulkit.weatherknow.BaseApplication;
import com.pulkit.weatherknow.utils.Images;

/**
 * Created by pulkit on 4/6/16.
 */
public class ImageDownloadStore
{
    private static final String IMAGES_PREF = "images_download_pref";
    private static final String CURRENT_IMAGE_INDEX = "image_url_index";

    private static SharedPreferences getImageDownloadPreferences()
    {
        return BaseApplication.getContext().getSharedPreferences(IMAGES_PREF, Context.MODE_PRIVATE);
    }

    public static void setCurrentImageIndex(int index)
    {
        Editor editor = getImageDownloadPreferences().edit();
        editor.putInt(CURRENT_IMAGE_INDEX, index);
        editor.apply();
    }

    public static String getCurrentImageUrl()
    {
        int index = getImageDownloadPreferences().getInt(CURRENT_IMAGE_INDEX, 0);
        if (index - 1 < 0)
        {
            return Images.URLS[0];
        } else
        {
            return Images.URLS[index - 1];
        }
    }

    public static int getNextImageUrlIndex()
    {
        int index = getImageDownloadPreferences().getInt(CURRENT_IMAGE_INDEX, 0);
        if (index >= Images.URLS.length)
        {
            setCurrentImageIndex(0);
            return 0;
        } else
        {
            setCurrentImageIndex(index + 1);
            return index;
        }
    }

    public static String getImageUrl()
    {
        return Images.URLS[getNextImageUrlIndex()];
    }
}
