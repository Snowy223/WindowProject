package WindowProject;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    Buttons buttons = new Buttons();

    ComboBox<String> cityComboBox, convertCToF, convertKphToMph;
    Label tempLabel, humidLabel, windLabel;
    HBox result;
    public void start(Stage primaryStage) {
        WindowBasics();
        SetScene(primaryStage);

        System.out.println(cityComboBox.getItems());

        cityComboBox.setOnAction(e -> {
            buttons.selectCityComboBox(cityComboBox, tempLabel, humidLabel, windLabel, convertCToF, convertKphToMph);
        });

        convertCToF.setOnAction(e -> {
            buttons.convertTempBoxButton(convertCToF, tempLabel);
        });

        convertKphToMph.setOnAction(e -> {
            buttons.convertSpeedBoxButton(convertKphToMph, windLabel);
        });
    }

    private void WindowBasics() {
        cityComboBox = new ComboBox<>();
        cityComboBox.getItems().addAll("New York", "London", "Vancouver", "Heccin Wimdy");

        tempLabel = new Label("Temperature: ");
        humidLabel = new Label("Humidity: ");
        windLabel = new Label("Wind Speed: ");

        VBox layout = new VBox(cityComboBox, tempLabel, humidLabel, windLabel);

        convertCToF = new ComboBox<>();
        convertCToF.getItems().addAll("Celsius", "Fahrenheit");
        convertCToF.setValue("Celsius");
        convertKphToMph = new ComboBox<>();
        convertKphToMph.getItems().addAll("KM/H", "MPH");
        convertKphToMph.setValue("KM/H");

        VBox tempLayout = new VBox(50);
        tempLayout.setAlignment(Pos.TOP_RIGHT);
        tempLayout.getChildren().addAll(convertCToF, convertKphToMph);

        result = new HBox(layout, tempLayout);
    }

    private void SetScene(Stage primaryStage) {
        primaryStage.setTitle("Weather Dashboard");
        Scene scene = new Scene(result, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

