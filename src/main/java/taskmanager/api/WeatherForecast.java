package taskmanager.api;

import java.time.LocalDateTime;

/**
 * Stores weather forecast information for one city and time.
 */
public class WeatherForecast {

    // City or location name for this forecast.
    private final String location;

    // Time when this forecast object was created or represents.
    private final LocalDateTime time;

    // Temperature value in Celsius.
    private final double temperatureCelsius;

    // Short weather text, such as "clear sky" or "rain".
    private final String condition;

    // Rain probability between 0.0 and 1.0.
    private final double precipitationProbability;

    /**
     * 1- Precondition: The caller provides weather values from a valid source.
     * 2- Postcondition: A WeatherForecast object is created with the provided values.
     * 3- Exceptions: This constructor does not validate values or throw exceptions directly.
     * 4- @param location The city or location name.
     * 4- @param time The forecast time.
     * 4- @param temperatureCelsius The temperature in Celsius.
     * 4- @param condition The weather condition text.
     * 4- @param precipitationProbability The rain probability from 0.0 to 1.0.
     * 5- @throws None directly.
     * 6- @return No direct return value because constructors create objects.
     *
     * @param location The city or location name.
     * @param time The forecast time.
     * @param temperatureCelsius The temperature in Celsius.
     * @param condition The weather condition text.
     * @param precipitationProbability The rain probability from 0.0 to 1.0.
     */
    public WeatherForecast(String location, LocalDateTime time,
                           double temperatureCelsius,
                           String condition,
                           double precipitationProbability) {
        this.location = location;
        this.time = time;
        this.temperatureCelsius = temperatureCelsius;
        this.condition = condition;
        this.precipitationProbability = precipitationProbability;
    }

    /**
     * 1- Precondition: The WeatherForecast object exists.
     * 2- Postcondition: The location is returned without changing the object.
     * 3- Exceptions: No exception is expected.
     * 4- @param None.
     * 5- @throws None.
     * 6- @return The forecast location.
     *
     * @return The forecast location.
     */
    public String getLocation() { return location; }

    /**
     * 1- Precondition: The WeatherForecast object exists.
     * 2- Postcondition: The forecast time is returned without changing the object.
     * 3- Exceptions: No exception is expected.
     * 4- @param None.
     * 5- @throws None.
     * 6- @return The forecast time.
     *
     * @return The forecast time.
     */
    public LocalDateTime getTime() { return time; }

    /**
     * 1- Precondition: The WeatherForecast object exists.
     * 2- Postcondition: The temperature is returned without changing the object.
     * 3- Exceptions: No exception is expected.
     * 4- @param None.
     * 5- @throws None.
     * 6- @return The temperature in Celsius.
     *
     * @return The temperature in Celsius.
     */
    public double getTemperatureCelsius() { return temperatureCelsius; }

    /**
     * 1- Precondition: The WeatherForecast object exists.
     * 2- Postcondition: The condition text is returned without changing the object.
     * 3- Exceptions: No exception is expected.
     * 4- @param None.
     * 5- @throws None.
     * 6- @return The weather condition text.
     *
     * @return The weather condition text.
     */
    public String getCondition() { return condition; }

    /**
     * 1- Precondition: The WeatherForecast object exists.
     * 2- Postcondition: The precipitation probability is returned without changing the object.
     * 3- Exceptions: No exception is expected.
     * 4- @param None.
     * 5- @throws None.
     * 6- @return Rain probability as a value from 0.0 to 1.0.
     *
     * @return Rain probability as a value from 0.0 to 1.0.
     */
    public double getPrecipitationProbability() { return precipitationProbability; }

    /**
     * 1- Precondition: The WeatherForecast object exists.
     * 2- Postcondition: A readable text version of the forecast is returned.
     * 3- Exceptions: No exception is expected.
     * 4- @param None.
     * 5- @throws None.
     * 6- @return A string containing the main forecast values.
     *
     * @return A string containing the main forecast values.
     */
    @Override
    public String toString() {
        return "WeatherForecast{location='" + location + "', condition='" + condition +
               "', temp=" + temperatureCelsius + "°C, rain=" + (precipitationProbability * 100) + "%}";
    }
}
