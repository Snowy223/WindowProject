package WindowProject;

import LocationData.LocationDataGetters;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.util.Objects;

public class Buttons {
    double displayedTemp = 0.0;
    double displayedWindSpeed = 0.0;
    static final String DEGREE_C = "°C";
    static final String DEGREE_F = "°F";
    static final String SPEED_KMH = " KM/H";
    static final String SPEED_MPH = " MPH";

    LocationDataGetters Data = new LocationDataGetters();
    void selectCityComboBox(ComboBox<String> cityComboBox, Label tempLabel, Label humidLabel, Label windLabel,
                            ComboBox<String> convertCToF, ComboBox<String> convertKphToMph) {

        double celsiusResult, humidityResult, windSpeedResult;
        String selectedCity = cityComboBox.getValue();

        switch(selectedCity) {
            case "New York":
                celsiusResult = Data.NewYork.Celsius;
                humidityResult = Data.NewYork.Humidity;
                windSpeedResult = Data.NewYork.WindSpeed;
                break;
            case "London":
                celsiusResult = Data.London.Celsius;
                humidityResult = Data.London.Humidity;
                windSpeedResult = Data.London.WindSpeed;
                break;
            case "Vancouver":
                celsiusResult = Data.Vancouver.Celsius;
                humidityResult = Data.Vancouver.Humidity;
                windSpeedResult = Data.Vancouver.WindSpeed;
                break;
            case "Heccin Wimdy":
                celsiusResult = Data.HeccinWimdy.Celsius;
                humidityResult = Data.HeccinWimdy.Humidity;
                windSpeedResult = Data.HeccinWimdy.WindSpeed;
                break;
            default:
                celsiusResult = 999;
                humidityResult = 999;
                windSpeedResult = 999;
                break;
        }

        String CorF = DEGREE_C;
        if("Fahrenheit".equals(convertCToF.getValue())) {
            displayedTemp = convertTemp(celsiusResult);
            CorF = DEGREE_F;
        }
        else displayedTemp = celsiusResult;
        tempLabel.setText("Temperature: " + displayedTemp + CorF);

        humidLabel.setText("Humidity: " + humidityResult + "%");

        String kphOrMph = SPEED_KMH;
        if(Objects.equals(convertKphToMph.getValue(), "MPH")) {
            displayedWindSpeed = convertSpeed(windSpeedResult);
            kphOrMph = SPEED_MPH;
        }
        else displayedWindSpeed = windSpeedResult;
        windLabel.setText("Wind speed: " + displayedWindSpeed + kphOrMph);
    }

    void convertTempBoxButton(ComboBox<String> convertCToF, Label tempLabel) {
        String CorF = DEGREE_C;
        String selectedTemp = convertCToF.getValue();

        switch(selectedTemp) {
            case "Celsius":
                displayedTemp = convertTempBack(displayedTemp);
                break;
            case "Fahrenheit":
                CorF = DEGREE_F;
                displayedTemp = convertTemp(displayedTemp);
                break;
        }
        tempLabel.setText("Temperature: " + displayedTemp + CorF);
    }

    void convertSpeedBoxButton(ComboBox<String> convertKphToMph, Label windLabel) {
        String kphOrMph = SPEED_KMH;
        String selectedSpeed = convertKphToMph.getValue();

        switch(selectedSpeed) {
            case "KM/H":
                displayedWindSpeed = convertSpeedBack(displayedWindSpeed);
                break;
            case "MPH":
                kphOrMph = SPEED_MPH;
                displayedWindSpeed = convertSpeed(displayedWindSpeed);
                break;
        }
        windLabel.setText("Wind speed: " + displayedWindSpeed + kphOrMph);
    }

    private static double convertSpeed(double WindSpeed) {
        return WindSpeed / 1.6;
    }

    private static double convertSpeedBack(double WindSpeed) {
        return WindSpeed * 1.6;
    }

    private static double convertTemp(double celsius) {
        return (celsius * ((double) 9 / 5)) + 32;
    }

    private static double convertTempBack(double fahrenheit) {
        return (fahrenheit - 32) * ((double) 5 / 9);
    }
}
