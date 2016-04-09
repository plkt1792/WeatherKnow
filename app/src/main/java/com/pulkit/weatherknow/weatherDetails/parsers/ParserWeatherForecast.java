package com.pulkit.weatherknow.weatherDetails.parsers;

import com.pulkit.weatherknow.entities.ForecastDetails;
import com.pulkit.weatherknow.entities.WeatherDetails;
import com.pulkit.weatherknow.network.exceptions.AccessDeniedException;
import com.pulkit.weatherknow.network.exceptions.ResourceNotFoundException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author pulkit
 */
public class ParserWeatherForecast
{

    public static final String CODE = "cod";
    public static final String NO_RESOURCE_CODE = "404";
    public static final String INVALID_API_KEY_CODE = "401";
    public static final String STATUS_OK_CODE = "200";
    public static final String CITY = "city";
    public static final String CITY_NAME = "name";
    public static final String CO_ORDINATE = "coord";
    public static final String LONGITUDE = "lon";
    public static final String LATITUDE = "lat";
    public static final String LIST = "list";
    public static final String TIMESTAMP = "dt";
    public static final String MAIN_WEATHER = "main";
    public static final String TEMP = "temp";
    public static final String TEMP_MIN = "temp_min";
    public static final String TEMP_MAX = "temp_max";
    public static final String PRESSURE = "pressure";
    public static final String SEA_LEVEL = "sea_level";
    public static final String GRND_LEVEL = "grnd_level";
    public static final String HUMIDITY = "humidity";
    public static final String WEATHER_DESC = "weather";
    public static final String WEATHER_DESCRIPTION = "description";
    public static final String CLOUDS = "clouds";
    public static final String CLOUD_PERCENT = "all";
    public static final String WIND = "wind";
    public static final String WIND_SPEED = "speed";
    public static final String WIND_DIRECTION_DEGREES = "deg";
    public static final String RAIN = "rain";
    public static final String LAST_3_HOURS_VALUE = "3h";
    public static final String SNOW = "snow";
    public static final String DATE_TIME = "dt_txt";

    public static ForecastDetails parse(String body) throws ResourceNotFoundException, AccessDeniedException
    {
        JSONObject result = null;
        try
        {
            result = new JSONObject(body);
            if (!result.isNull(CODE))
            {
                String code = result.getString(CODE);
                switch (code)
                {
                    case NO_RESOURCE_CODE:
                        throw new ResourceNotFoundException();
                    case INVALID_API_KEY_CODE:
                        throw new AccessDeniedException();
                    case STATUS_OK_CODE:
                        return parseResponse(result);
                    default:
                        //do nothing
                }
            }
            return null;
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private static ForecastDetails parseResponse(JSONObject result)
    {
        if (result.isNull(CITY))
        {
            return null;
        }

        try
        {
            ForecastDetails forecastDetails = new ForecastDetails();
            JSONObject cityObject = result.getJSONObject(CITY);
            if (!cityObject.isNull(CITY_NAME))
            {
                forecastDetails.setCity(cityObject.getString(CITY_NAME));
            }
            if (!cityObject.isNull(CO_ORDINATE))
            {
                JSONObject coordObj = cityObject.getJSONObject(CO_ORDINATE);
                if (!coordObj.isNull(LONGITUDE))
                {
                    forecastDetails.setLongitude(coordObj.getDouble(LONGITUDE));
                }
                if (!coordObj.isNull(LATITUDE))
                {
                    forecastDetails.setLatitude(coordObj.getDouble(LATITUDE));
                }
            }

            if (!result.isNull(LIST))
            {
                JSONArray weatherJSONArray = result.getJSONArray(LIST);
                if (weatherJSONArray.length() == 0)
                {
                    return null;
                }
                ArrayList<WeatherDetails> weatherList = new ArrayList<>(40);
                for (int i = 0; i < weatherJSONArray.length(); i++)
                {
                    JSONObject weatherObj = weatherJSONArray.getJSONObject(i);
                    WeatherDetails weatherDetails = new WeatherDetails();
                    if (!weatherObj.isNull(TIMESTAMP))
                    {
                        weatherDetails.setCurrentTimeStamp(weatherObj.getLong(TIMESTAMP));
                    }
                    if (!weatherObj.isNull(DATE_TIME))
                    {
                        weatherDetails.setDateTxt(weatherObj.getString(DATE_TIME));
                    }
                    if (!weatherObj.isNull(MAIN_WEATHER))
                    {
                        JSONObject main = weatherObj.getJSONObject(MAIN_WEATHER);
                        if (!main.isNull(TEMP))
                        {
                            weatherDetails.setCurrentTemp(main.getDouble(TEMP));
                        }
                        if (!main.isNull(TEMP_MIN))
                        {
                            weatherDetails.setMinTemp(main.getDouble(TEMP_MIN));
                        }
                        if (!main.isNull(TEMP_MAX))
                        {
                            weatherDetails.setMaxTemp(main.getDouble(TEMP_MAX));
                        }
                        if (!main.isNull(PRESSURE))
                        {
                            weatherDetails.setPressure(main.getDouble(PRESSURE));
                        }
                        if (!main.isNull(SEA_LEVEL))
                        {
                            weatherDetails.setSeaLevel(main.getDouble(SEA_LEVEL));
                        }
                        if (!main.isNull(GRND_LEVEL))
                        {
                            weatherDetails.setGroundLevel(main.getDouble(GRND_LEVEL));
                        }
                        if (!main.isNull(HUMIDITY))
                        {
                            weatherDetails.setHumidityPercentage(main.getDouble(HUMIDITY));
                        }
                    }
                    if (!weatherObj.isNull(WEATHER_DESC))
                    {
                        JSONObject weatherDescObj = weatherObj.getJSONArray(WEATHER_DESC).getJSONObject(0);
                        if (!weatherDescObj.isNull(MAIN_WEATHER))
                        {
                            weatherDetails.setWeatherType(weatherDescObj.getString(MAIN_WEATHER));
                        }
                        if (!weatherDescObj.isNull(WEATHER_DESCRIPTION))
                        {
                            weatherDetails.setWeatherDescription(weatherDescObj.getString(WEATHER_DESCRIPTION));
                        }
                    }
                    if (!weatherObj.isNull(CLOUDS))
                    {
                        JSONObject cloudObj = weatherObj.getJSONObject(CLOUDS);
                        if (!cloudObj.isNull(CLOUD_PERCENT))
                        {
                            weatherDetails.setCloudPercentage(cloudObj.getDouble(CLOUD_PERCENT));
                        }
                    }
                    if (!weatherObj.isNull(WIND))
                    {
                        JSONObject windObj = weatherObj.getJSONObject(WIND);
                        if (!windObj.isNull(WIND_SPEED))
                        {
                            weatherDetails.setWindSpeed(windObj.getDouble(WIND_SPEED));
                        }
                        if (!windObj.isNull(WIND_DIRECTION_DEGREES))
                        {
                            weatherDetails.setWindDirectionDegrees(windObj.getDouble(WIND_DIRECTION_DEGREES));
                        }
                    }
                    if (!weatherObj.isNull(RAIN))
                    {
                        JSONObject rainObj = weatherObj.getJSONObject(RAIN);
                        if (!rainObj.isNull(LAST_3_HOURS_VALUE))
                        {
                            weatherDetails.setRainIn3Hours(rainObj.getDouble(LAST_3_HOURS_VALUE));
                        }
                    }
                    if (!weatherObj.isNull(SNOW))
                    {
                        JSONObject snowObj = weatherObj.getJSONObject(SNOW);
                        if (!snowObj.isNull(LAST_3_HOURS_VALUE))
                        {
                            weatherDetails.setSnowIn3Hours(snowObj.getDouble(LAST_3_HOURS_VALUE));
                        }
                    }
                    weatherList.add(weatherDetails);
                }
                forecastDetails.setWeatherDetails(weatherList);
            }
            return forecastDetails;

        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
