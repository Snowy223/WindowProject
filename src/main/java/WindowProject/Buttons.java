package WindowProject;

import java.sql.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import java.util.Objects;

public class Buttons {
    static final String DEGREE_C = "\u00B0C"; // \u00B0 is the degrees symbol °
    static final String DEGREE_F = "\u00B0F";
    static final String SPEED_KMH = " KM/H";
    static final String SPEED_MPH = " MPH";

    private String cityWeather; // This variable will be used later
    private double cityTemperature, cityWindSpeed;
    private int cityHumidity;

    // File entrypoint. File manages behavior for button presses (including ComboBoxes)
    // with selectCityComboBox(), convertTempButtonBox(), and convertSpeedButtonBox()
    void selectCityComboBox(ComboBox<String> cityComboBox, Label tempLabel, Label humidLabel, Label windLabel,
    ComboBox<String> convertCToF, ComboBox<String> convertKphToMph, Connection conn) {
        String selectedCity = cityComboBox.getValue();

        getSelectedCityData(conn, selectedCity);
        setWeatherData(tempLabel, humidLabel, windLabel, convertCToF, convertKphToMph);
    }

    // Grabs data for city selected in the ComboBox
    private void getSelectedCityData(Connection conn, String selectedCity) {
        try(Statement stmt = conn.createStatement()) {
            stmt.execute("USE globalinfo;");
            ResultSet sqlSelect = stmt.executeQuery("SELECT * FROM city WHERE name='" + selectedCity + "';");
            sqlSelect.next();
            cityWeather = sqlSelect.getString("weathertype");
            cityTemperature = sqlSelect.getDouble("temperature");
            cityHumidity = sqlSelect.getInt("humidity");
            cityWindSpeed = sqlSelect.getDouble("windspeed");
        }
        catch(Exception e) {
            System.out.println("Failed to get city column from continent foreign key");
        }
    }

    // Sets the data which is displayed on-screen in the weather app
    private void setWeatherData(Label tempLabel, Label humidLabel, Label windLabel,
    ComboBox<String> convertCToF, ComboBox<String> convertKphToMph) {
        // Converts cityTemperature to Fahrenheit if Fahrenheit is selected in the convertCToF ComboBox. Else, does nothing
        String CorF = DEGREE_C;
        if("Fahrenheit".equals(convertCToF.getValue())) {
            cityTemperature = convertTemp(cityTemperature);
            CorF = DEGREE_F;
        }
        // Set the Temperature for label with current temperature for specified city
        tempLabel.setText("Temperature: " + cityTemperature + CorF);

        // Set the Humidity for label with current humidity for specified city
        humidLabel.setText("Humidity: " + cityHumidity + "%");

        // Converts cityWindSpeed to MPH if MPH is selected in the convertKphToMph ComboBox. Else, does nothing
        String kphOrMph = SPEED_KMH;
        if(Objects.equals(convertKphToMph.getValue(), "MPH")) {
            cityWindSpeed = cityWindSpeed / 1.6;
            kphOrMph = SPEED_MPH;
        }
        // Set the Wind Speed for label with current wind speed for specified city
        windLabel.setText("Wind speed: " + cityWindSpeed + kphOrMph);
    }

    // ComboBox for selecting Celsius or Fahrenheit measurements
    void convertTempComboBox(ComboBox<String> convertCToF, Label tempLabel) {
        String CorF = DEGREE_C;
        String selectedTemp = convertCToF.getValue();
        switch(selectedTemp) {
            case "Celsius":
                cityTemperature = convertTempBack(cityTemperature);
                break;
            case "Fahrenheit":
                CorF = DEGREE_F;
                cityTemperature = convertTemp(cityTemperature);
                break;
        }
        tempLabel.setText("Temperature: " + cityTemperature + CorF);
    }

    // ComboBox for selecting Kilometers or Miles per hour
    void convertSpeedComboBox(ComboBox<String> convertKphToMph, Label windLabel) {
        String kphOrMph = SPEED_KMH;
        String selectedSpeed = convertKphToMph.getValue();
        switch(selectedSpeed) {
            case "KM/H":
                cityWindSpeed = cityWindSpeed * 1.6;
                break;
            case "MPH":
                kphOrMph = SPEED_MPH;
                cityWindSpeed = cityWindSpeed / 1.6;
                break;
        }
        windLabel.setText("Wind speed: " + cityWindSpeed + kphOrMph);
    }

    private static double convertTemp(double celsius) {
        return (celsius * ((double) 9 / 5)) + 32;
    }

    private static double convertTempBack(double fahrenheit) {
        return (fahrenheit - 32) * ((double) 5 / 9);
    }
}
