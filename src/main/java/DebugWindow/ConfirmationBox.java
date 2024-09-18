package DebugWindow;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ConfirmationBox extends Stage {
    private VBox vbox;
    private final Button confirmButton = new Button("Confirm");
    public ConfirmationBox(int desiredOutput, String selectedContinent, String selectedCountry, String selectedCity) {
        setTitle("Are you sure?");

        switch(desiredOutput) {
            case 1:
                addContinentConfirm(selectedContinent);
                break;
            case 2:
                addCountryConfirm(selectedContinent, selectedCountry);
                break;
            case 3:
                addCityConfirm(selectedCountry, selectedCity);
                break;
            default:
                return;
        }
        setButtons();

        Scene sc = new Scene(vbox);
        setResizable(false);
        setScene(sc);
        show();
    }

    private void addContinentConfirm(String selectedContinent) {
        Label confirmation = new Label("Are you sure you want to add the new continent \"" + selectedContinent + "\"?");
        vbox = new VBox(confirmation);

        confirmButton.setOnAction(e -> {
            addContinent.addToSQL();
        });
    }

    private void addCountryConfirm(String selectedContinent, String selectedCountry) {
        Label confirmation = new Label("Are you sure you want to add the new country \"" + selectedCountry +
        "\" in continent \"" + selectedContinent + "\"");
        vbox = new VBox(confirmation);

        confirmButton.setOnAction(e -> {
            addCountry.addToSQL();
        });
    }

    private void addCityConfirm(String selectedCountry, String selectedCity) {
        Label confirmation = new Label("Are you sure you want to add then new city \"" + selectedCity +
        "\" in country \"" + selectedCountry + "\"?");
        vbox = new VBox(confirmation);

        confirmButton.setOnAction(e -> {
            addCity.addToSQL();
        });
    }

    private void setButtons() {
        Button cancelButton = new Button("Cancel");

        cancelButton.setOnAction(e -> {
            close();
        });

        HBox buttonBox = new HBox(confirmButton, cancelButton);
        vbox.getChildren().add(buttonBox);
    }
}
