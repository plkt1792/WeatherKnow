package com.pulkit.weatherknow.network.exceptions;

/**
 * @author pulkit
 * To be thrown when server request returns non-successful status code (not between 200-300).
 */
public class HttpException extends Exception
{

    public HttpException(String message)
    {
        super(message);
    }
}
