package com.pulkit.weatherknow.network.exceptions;

import com.pulkit.weatherknow.utils.Constants;

/**
 * @author pulkit
 * To be thrown when server request returns 404 response code.
 */
public class ResourceNotFoundException extends HttpException
{

    public ResourceNotFoundException ()
    {
        super(Constants.RESOURCE_NOT_FOUND);
    }

}
