package DebugWindow;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/*
This entire file is a confirmation box making sure the inputted query is okay, and makes sure that there's
extra dialogue before executing the full Query on the MariaDB server. Making this file made me think that
it would be more secure to have a server host a program which interprets the SQL data, and translates it
into API data for this program to use, instead of this program connecting itself directly to the MariaDB
database, but I'm too lazy to actually implement that, so maybe that will be added as a feature some other
time, or maybe I'll get bored and add it myself later on today idk
*/

public class ConfirmationBox extends Stage {
    private VBox vbox;
    private String newItem, selection;
    private boolean returnedError = false;
    private final Button confirmButton = new Button("Confirm"), cancelButton = new Button("Cancel");
    // File entrypoint, constructor spawns a confirmation box when adding an SQL query to the MariaDB server
    public ConfirmationBox(int desiredOutput, String selectedContinent, String selectedCountry, String selectedCity) {
        setTitle("Are you sure?");

        // When ConfirmationBox is called, set desiredOutput variable to select 1 (continent), 2 (country), or 3 (city)
        // Also sets functionality of "confirm" button
        desiredOutput(desiredOutput, selectedContinent, selectedCountry, selectedCity);
        if(returnedError) return;

        // Functionality to spawn a Label object customized for continent, city, or country
        confirmationLabel(desiredOutput, selectedContinent, selectedCountry);

        // Functionality for cancelButton and creating a HBox to link confirmButton and cancelButton together
        cancelButton.setOnAction(e -> close());
        HBox buttonBox = new HBox(confirmButton, cancelButton);
        vbox.getChildren().add(buttonBox);

        // Scene stuff blah blah
        Scene sc = new Scene(vbox);
        setResizable(false);
        setScene(sc);
        show();
    }

    // Defines if a continent, country, or city is being added to the MariaDB server and program Confirm button depending on output
    private void desiredOutput(int desiredOutput,
    String selectedContinent, String selectedCountry, String selectedCity) {
        switch(desiredOutput) {
            case 1:
                newItem = "continent";
                selection = selectedContinent;
                confirmButton.setOnAction(e -> addContinent.addToSQL());
                break;
            case 2:
                newItem = "country";
                selection = selectedCountry;
                confirmButton.setOnAction(e -> addCountry.addToSQL());
                break;
            case 3:
                newItem = "city";
                selection = selectedCity;
                confirmButton.setOnAction(e -> addCity.addToSQL());
                break;
            default:
                System.out.println("Failed to supply a valid desiredOutput result: " + desiredOutput);
                returnedError = true;
        }
    }

    // Spawns a Label object asking for confirmation, customized based on desiredOutput variable input
    private void confirmationLabel(int desiredOutput, String selectedContinent, String selectedCountry) {
        Label confirmation;
        // Define the confirmation Label for continent
        if(desiredOutput == 1) {
            confirmation = new Label("Are you sure you want to add the new " + newItem + "\"" + selection + "\"?");
        }
        // Define the confirmation Label for country or city
        else {
            // Sets parent object type and name of parent object for reference when Label object is created
            // selectionParent (type): "country" or "city"
            // selectionParentName (name): any one of the 7 provided continents
            String selectionParent = "country";
            String selectionParentName;
            if(newItem.equals("country")) {
                selectionParent = "continent";
                selectionParentName = selectedContinent;
            }
            else selectionParentName = selectedCountry;
            confirmation = new Label("Are you sure you want to add the new " + newItem + "\"" + selection + "\" to "
                    + selectionParent + "\"" + selectionParentName);
        }
        vbox = new VBox(confirmation);
    }
}
