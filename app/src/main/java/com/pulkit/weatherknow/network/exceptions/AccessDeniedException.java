package com.pulkit.weatherknow.network.exceptions;


import com.pulkit.weatherknow.utils.Constants;

/**
 * @author pulkit
 */
public class AccessDeniedException extends HttpException
{

    public AccessDeniedException()
    {
        super(Constants.API_ACCESS_DENIED);
    }
}
