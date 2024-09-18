package WindowProject;

import JSONManager.Config;
import JSONManager.DatabaseConfig;

import java.io.IOException;
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

/*
------------------------
Example SQL setup:

Create SQL database:    "CREATE DATABASE globalinfo;"
Use SQL database:       "USE globalinfo;"
Create continent table: "CREATE TABLE continent(id INT PRIMARY KEY AUTO_INCREMENT, name varchar(255));
Create country table:   "CREATE TABLE country(id INT PRIMARY KEY AUTO_INCREMENT, name varchar(255), continent_id INT, FOREIGN KEY (continent_id) REFERENCES continent(id));
Create city table:      "CREATE TABLE city(id INT PRIMARY KEY AUTO_INCREMENT, name varchar(255), country_id INT, FOREIGN KEY (country_id) REFERENCES country(id));

To add contents to these tables:
INSERT INTO continent(name) VALUES("North America");
INSERT INTO country(name, continent_id) VALUES("United States", 1);
INSERT INTO city(name, country_id) VALUES("New York", 1);
INSERT INTO city(name, country_id) VALUES("Philadelphia", 1);
------------------------

Username and password of MariaDB server are located in config.json, found in program's root directory. An example is
included in the build project. If you want to try this for yourself, create a MariaDB database and input credentials
"url", "username", and "password" into config.json. URL for localhost will always be jdbc:mariadb://127.0.0.1:3306/
*/

public class Main extends Application {
    private String url, username, password;
    private Properties connConfig;
    private Connection conn;

    Buttons buttons = new Buttons();

    ComboBox<String> cityComboBox, convertCToF, convertKphToMph;
    Label tempLabel, humidLabel, windLabel;
    HBox result;
    public void start(Stage primaryStage) {
        // Start hby loading MariaDB credentials from config.json file
        try {
            loadJSONData();
        }
        catch(Exception e) {
            System.out.println("Failed to set up JSON data");
            e.printStackTrace();
            return;
        }

        // Establish a connection to MariaDB server
        establishMariaConn();

        // Setup displayed window
        WindowBasics();
        SetScene(primaryStage);

        // Setup functionality for the city selection Combo Box
        cityComboBox.setOnAction(e -> {
            buttons.selectCityComboBox(cityComboBox, tempLabel, humidLabel, windLabel, convertCToF, convertKphToMph, conn);
        });

        // Setup functionality for the temp conversion Combo Box
        convertCToF.setOnAction(e -> {
            buttons.convertTempComboBox(convertCToF, tempLabel);
        });

        // Setup functionality for the speed conversion Combo Box
        convertKphToMph.setOnAction(e -> {
            buttons.convertSpeedComboBox(convertKphToMph, windLabel);
        });
    }

    // Sets up the layout of the window
    private void WindowBasics() {
        cityComboBox = new ComboBox<>();

        // Connect to MariaDB server to retrieve NA cities
        // TODO add more continents later on
        try(Statement stmt = conn.createStatement()) {
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

        // Column to display the city selection combo box, and gathered data from MariaDB server
        VBox layout = new VBox(cityComboBox, tempLabel, humidLabel, windLabel);
        layout.setSpacing(2);
        layout.setMinWidth(150);

        // ComboBox for switching temperature between Celsius and Fahrenheit
        convertCToF = new ComboBox<>();
        convertCToF.getItems().addAll("Celsius", "Fahrenheit");
        convertCToF.setValue("Celsius");
        // ComboBox for switching between KM/H and MPH
        convertKphToMph = new ComboBox<>();
        convertKphToMph.getItems().addAll("KM/H", "MPH");
        convertKphToMph.setValue("KM/H");

        // Add convertCTof and convertKphToMph to a new column
        VBox tempLayout = new VBox(10);
        tempLayout.setAlignment(Pos.TOP_RIGHT);
        tempLayout.getChildren().addAll(convertCToF, convertKphToMph);
        tempLayout.setMinWidth(40);
        tempLayout.setMaxWidth(Double.MAX_VALUE);

        // Make VBox columns side-by-side with HBox row
        result = new HBox(layout, tempLayout);
        result.setSpacing(50);
    }

    // Handles important window data
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

    // Sends a connection request to supplied MariaDB server, will fail with invalid credentials
    private void establishMariaConn() {
        connConfig = new Properties();
        connConfig.setProperty("user", username);
        connConfig.setProperty("password", password);

        try {
            conn = DriverManager.getConnection(url, connConfig);
        }
        catch(Exception e) {
            System.out.println("Connection to MariaDB server failed");
        }
    }

    // Loads JSON data from config.json, requires url, username, and password variables in an Object named "database"
    private void loadJSONData() throws IOException {
        Config config = Config.loadConfig("config.json");
        DatabaseConfig dbConfig = config.getDatabase();

        url = dbConfig.getUrl();
        username = dbConfig.getUsername();
        password = dbConfig.getPassword();
    }
}
