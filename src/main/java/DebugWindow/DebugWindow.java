package DebugWindow;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DebugWindow extends Stage {
    private Button addContinent, addCountry, addCity, goBack;
    public DebugWindow() {
        setupDebugWindow();
        setupButtonFunctionality();
        show();
    }

    private void setupDebugWindow() {
        setTitle("Debug menu");

        Label welcomeMenu = new Label("Welcome to the debug menu! What would you like to do?");

        Region welcomeSpacer = new Region();
        welcomeSpacer.setMinHeight(50);

        addContinent = new Button("Add continent");
        addCountry = new Button("Add country");
        addCity = new Button("Add city");

        Region backButtonSpacer = new Region();
        backButtonSpacer.setMinHeight(20);


        goBack = new Button("Go back");

        VBox vbox = new VBox(welcomeMenu, welcomeSpacer, addContinent, addCountry, addCity, backButtonSpacer, goBack);
        vbox.setSpacing(10);

        Scene sc = new Scene(vbox);
        setResizable(false);
        setScene(sc);
    }

    private void setupButtonFunctionality() {
        addContinent.setOnAction(e -> {
            Stage addContinent = new addContinent();
            close();
        });
        addCountry.setOnAction(e -> {
            Stage addCountry = new addCountry();
            close();
        });
        addCity.setOnAction(e -> {
            Stage addCity = new addCity();
            close();
        });
        // I'll figure this one out later... TODO
        goBack.setOnAction(e -> {

        });
    }

    static String contextMenu(String type) {
        return "This window is used for adding a " + type + " to the MariaDB database!";
    }
}
