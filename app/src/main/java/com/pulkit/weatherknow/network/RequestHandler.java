package com.pulkit.weatherknow.network;

import com.pulkit.weatherknow.BaseApplication;
import com.pulkit.weatherknow.network.exceptions.AccessDeniedException;
import com.pulkit.weatherknow.network.exceptions.HttpException;
import com.pulkit.weatherknow.network.exceptions.NoNetworkException;
import com.pulkit.weatherknow.network.exceptions.ResourceNotFoundException;
import com.pulkit.weatherknow.network.exceptions.ServerErrorException;
import com.pulkit.weatherknow.utils.Constants;
import com.pulkit.weatherknow.utils.UtilityClass;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author pulkit
 */
public class RequestHandler
{

    public static String makeRequest(Request request) throws IOException, HttpException, NoNetworkException
    {
        if (UtilityClass.isNetworkAvailable(BaseApplication.getContext()))
        {
            return doRequest(request);
        } else
        {
            throw new NoNetworkException(Constants.NETWORK_ERROR);
        }
    }

    private static String doRequest(Request request) throws IOException, HttpException
    {
        OkHttpClient httpClient = HttpClientFactory.getClient();
        Response response = httpClient.newCall(request).execute();
        String body = response.body().string();
        if (!response.isSuccessful())
        {
            if (response.code() == 400)
            {
                throw new HttpException(Constants.BAD_REQUEST);
            } else if (response.code() == 401)
            {
                throw new AccessDeniedException();
            } else if (response.code() == 404)
            {
                throw new ResourceNotFoundException();
            } else if (response.code() >= 500)
            {
                throw new ServerErrorException();
            } else
            {
                throw new HttpException(Constants.SERVER_ERROR);
            }
        } else
        {
            return body;
        }
    }
}
