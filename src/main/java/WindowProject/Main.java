package WindowProject;

import LocationData.LocationDataGetters;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    private static final String DEGREE_C = "°C";
    private static final String DEGREE_F = "°F";
    private static final String SPEED_KMH = " KM/H";
    private static final String SPEED_MPH = " MPH";


    double displayedTemp = 0.0;
    double displayedWindSpeed = 0.0;

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Weather Dashboard");

        ComboBox<String> cityComboBox = new ComboBox<>();
        cityComboBox.getItems().addAll("New York", "London", "Vancouver");

        Label tempLabel = new Label("Temperature: ");
        Label humidLabel = new Label("Humidity: ");
        Label windLabel = new Label("Wind Speed: ");

        VBox layout = new VBox(cityComboBox, tempLabel, humidLabel, windLabel);

        ComboBox<String> convertCToF = new ComboBox<>();
        convertCToF.getItems().addAll("Celsius", "Fahrenheit");
        convertCToF.setValue("Celsius");
        ComboBox<String> convertKphToMph = new ComboBox<>();
        convertKphToMph.getItems().addAll("KM/H", "MPH");
        convertKphToMph.setValue("KM/H");

        VBox tempLayout = new VBox(10);
        tempLayout.setAlignment(Pos.TOP_RIGHT);
        tempLayout.getChildren().addAll(convertCToF, convertKphToMph);

        HBox result = new HBox(layout, tempLayout);

        Scene scene = new Scene(result, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        LocationDataGetters Data = new LocationDataGetters();
        cityComboBox.setOnAction(e -> {
            selectCityComboBox(cityComboBox, tempLabel, humidLabel, windLabel, convertCToF, convertKphToMph, Data);
        });

        convertCToF.setOnAction(e -> {
            convertTempBoxButton(convertCToF, tempLabel);
        });

        convertKphToMph.setOnAction(e -> {
            convertSpeedBoxButton(convertKphToMph, windLabel);
        });
    }

    void selectCityComboBox(ComboBox<String> cityComboBox, Label tempLabel, Label humidLabel, Label windLabel,
                            ComboBox<String> convertCToF, ComboBox<String> convertKphToMph, LocationDataGetters Data) {

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
            default:
                celsiusResult = Data.Vancouver.Celsius;
                humidityResult = Data.Vancouver.Humidity;
                windSpeedResult = Data.Vancouver.WindSpeed;
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

    public static void main(String[] args) {
        launch(args);
    }
}

