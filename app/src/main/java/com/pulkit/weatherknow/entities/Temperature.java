package com.pulkit.weatherknow.entities;

import java.io.Serializable;

/**
 * Created by pulkit on 4/6/16.
 */
public class Temperature implements Serializable
{
    private double min;
    private double max;
    private double feelsLike;

    public double getMin()
    {
        return min;
    }

    public void setMin(double min)
    {
        this.min = min;
    }

    public double getMax()
    {
        return max;
    }

    public void setMax(double max)
    {
        this.max = max;
    }

    public double getFeelsLike()
    {
        return feelsLike;
    }

    public void setFeelsLike(double feelsLike)
    {
        this.feelsLike = feelsLike;
    }
}
