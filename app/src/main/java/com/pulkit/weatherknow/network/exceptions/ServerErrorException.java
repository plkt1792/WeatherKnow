package com.pulkit.weatherknow.network.exceptions;

import com.pulkit.weatherknow.utils.Constants;

/**
 * @author pulkit
 * To be thrown when server request returns response code > 500.
 */
public class ServerErrorException extends HttpException
{

    public ServerErrorException ()
    {
        super(Constants.SERVER_ERROR);
    }

}
