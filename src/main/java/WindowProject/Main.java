package WindowProject;

import java.sql.*;
import java.util.Properties;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    private Properties connConfig;
    private Connection conn;

    Buttons buttons = new Buttons();

    ComboBox<String> cityComboBox, convertCToF, convertKphToMph;
    Label tempLabel, humidLabel, windLabel;
    HBox result;
    public void start(Stage primaryStage) {
        establishMariaConn();

        WindowBasics();
        SetScene(primaryStage);

        System.out.println(cityComboBox.getItems());

        cityComboBox.setOnAction(e -> {
            buttons.selectCityComboBox(cityComboBox, tempLabel, humidLabel, windLabel, convertCToF, convertKphToMph, conn);
        });

        convertCToF.setOnAction(e -> {
            buttons.convertTempComboBox(convertCToF, tempLabel);
        });

        convertKphToMph.setOnAction(e -> {
            buttons.convertSpeedComboBox(convertKphToMph, windLabel);
        });
    }

    private void WindowBasics() {
        cityComboBox = new ComboBox<>();

        // Connect to MariaDB server to retrieve NA cities
        try(Statement stmt = conn.createStatement()) {
            // Unnecessarily long query for getting all city names based on continent
            stmt.execute("USE globalinfo;");
            ResultSet CitiesList = stmt.executeQuery(
            "SELECT city.name FROM city " +
                "JOIN country ON city.country_id = country.id " +
                "JOIN continent ON country.continent_id = continent.id " +
                "WHERE continent.name = 'North America';"
            );

            while(CitiesList.next()) {
                String city = CitiesList.getString("name");
                cityComboBox.getItems().add(city);
            }
        }
        catch(Exception e) {
            System.out.println("Failed to get city column from continent foreign key");
        }

        // Temporary values to make sure the app doesn't look weird on launch
        tempLabel = new Label("Temperature: 0.0" + Buttons.DEGREE_C);
        humidLabel = new Label("Humidity: 0%");
        windLabel = new Label("Wind Speed: 0.0" + Buttons.SPEED_KMH);

        VBox layout = new VBox(cityComboBox, tempLabel, humidLabel, windLabel);
        layout.setSpacing(2);
        layout.setMinWidth(150);

        convertCToF = new ComboBox<>();
        convertCToF.getItems().addAll("Celsius", "Fahrenheit");
        convertCToF.setValue("Celsius");
        convertKphToMph = new ComboBox<>();
        convertKphToMph.getItems().addAll("KM/H", "MPH");
        convertKphToMph.setValue("KM/H");

        VBox tempLayout = new VBox(10);
        tempLayout.setAlignment(Pos.TOP_RIGHT);
        tempLayout.getChildren().addAll(convertCToF, convertKphToMph);
        tempLayout.setMinWidth(40);

        tempLayout.setMaxWidth(Double.MAX_VALUE);
        result = new HBox(layout, tempLayout);
        result.setSpacing(50);
    }

    private void SetScene(Stage primaryStage) {
        primaryStage.setTitle("Weather Dashboard");
        Scene scene = new Scene(result, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void establishMariaConn() {
        connConfig = new Properties();
        connConfig.setProperty("user", "root");
        connConfig.setProperty("password", "password");

        try {
            conn = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/", connConfig);
        }
        catch(Exception e) {
            System.out.println("Connection to MariaDB server failed");
        }
    }
}

