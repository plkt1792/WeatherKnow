package com.pulkit.weatherknow.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author pulkit
 */
public class ForecastDetails implements Serializable
{

    private String city;
    private double latitude;
    private double longitude;
    private ArrayList<WeatherDetails> weatherDetails;
    private HashMap<Integer, Temperature> temperatureForecast;

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public ArrayList<WeatherDetails> getWeatherDetails()
    {
        return weatherDetails;
    }

    public void setWeatherDetails(ArrayList<WeatherDetails> weatherDetails)
    {
        this.weatherDetails = weatherDetails;
    }

    public HashMap<Integer, Temperature> getTemperatureForecast()
    {
        return temperatureForecast;
    }

    public void setTemperatureForecast(HashMap<Integer, Temperature> temperatureForecast)
    {
        this.temperatureForecast = temperatureForecast;
    }
}
