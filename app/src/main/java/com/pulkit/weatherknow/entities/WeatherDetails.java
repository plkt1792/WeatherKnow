package com.pulkit.weatherknow.entities;

import java.io.Serializable;

/**
 * @author pulkit
 */
public class WeatherDetails implements Serializable
{
    private long currentTimeStamp;
    private String dateTxt;
    private double currentTemp;
    private double maxTemp;
    private double minTemp;
    private double pressure = -1;
    private double seaLevel = -1;
    private double groundLevel = -1;
    private double humidityPercentage = -1;
    private String weatherType;
    private String weatherDescription;
    private double cloudPercentage = -1;
    private double windSpeed = -1;
    private double windDirectionDegrees = -1;
    private double rainIn3Hours = -1;
    private double snowIn3Hours = -1;

    public long getCurrentTimeStamp()
    {
        return currentTimeStamp;
    }

    public void setCurrentTimeStamp(long currentTimeStamp)
    {
        this.currentTimeStamp = currentTimeStamp;
    }

    public String getDateTxt()
    {
        return dateTxt;
    }

    public void setDateTxt(String dateTxt)
    {
        this.dateTxt = dateTxt;
    }

    public double getCurrentTemp()
    {
        return currentTemp;
    }

    public void setCurrentTemp(double currentTemp)
    {
        this.currentTemp = currentTemp;
    }

    public double getMaxTemp()
    {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp)
    {
        this.maxTemp = maxTemp;
    }

    public double getMinTemp()
    {
        return minTemp;
    }

    public void setMinTemp(double minTemp)
    {
        this.minTemp = minTemp;
    }

    public double getPressure()
    {
        return pressure;
    }

    public void setPressure(double pressure)
    {
        this.pressure = pressure;
    }

    public double getSeaLevel()
    {
        return seaLevel;
    }

    public void setSeaLevel(double seaLevel)
    {
        this.seaLevel = seaLevel;
    }

    public double getGroundLevel()
    {
        return groundLevel;
    }

    public void setGroundLevel(double groundLevel)
    {
        this.groundLevel = groundLevel;
    }

    public double getHumidityPercentage()
    {
        return humidityPercentage;
    }

    public void setHumidityPercentage(double humidityPercentage)
    {
        this.humidityPercentage = humidityPercentage;
    }

    public String getWeatherType()
    {
        return weatherType;
    }

    public void setWeatherType(String weatherType)
    {
        this.weatherType = weatherType;
    }

    public String getWeatherDescription()
    {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription)
    {
        this.weatherDescription = weatherDescription;
    }

    public double getCloudPercentage()
    {
        return cloudPercentage;
    }

    public void setCloudPercentage(double cloudPercentage)
    {
        this.cloudPercentage = cloudPercentage;
    }

    public double getWindSpeed()
    {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed)
    {
        this.windSpeed = windSpeed;
    }

    public double getWindDirectionDegrees()
    {
        return windDirectionDegrees;
    }

    public void setWindDirectionDegrees(double windDirectionDegrees)
    {
        this.windDirectionDegrees = windDirectionDegrees;
    }

    public double getRainIn3Hours()
    {
        return rainIn3Hours;
    }

    public void setRainIn3Hours(double rainIn3Hours)
    {
        this.rainIn3Hours = rainIn3Hours;
    }

    public double getSnowIn3Hours()
    {
        return snowIn3Hours;
    }

    public void setSnowIn3Hours(double snowIn3Hours)
    {
        this.snowIn3Hours = snowIn3Hours;
    }
}
