package DebugWindow;

import WindowProject.Main;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class addCountry extends Stage {
    private static TextField countryInput;
    private static TextField continentInput;
    private Button confirmBox, cancelBox;

    public addCountry() {
        setupCountryWindow();
        setupButtonFunctionality();
        show();
    }

    private void setupCountryWindow() {
        setTitle("Add country");

        Label contextLabel = new Label(DebugWindow.contextMenu("country"));

        Label countryName = new Label("Country: ");
        countryInput = new TextField();
        HBox countryBox = new HBox(countryName, countryInput);

        Label continentName = new Label("Continent of country: ");
        continentInput = new TextField();
        HBox continentBox = new HBox(continentName, continentInput);

        Region confirmSpacer = new Region();
        confirmSpacer.setMinWidth(50);

        confirmBox = new Button("Confirm");
        cancelBox = new Button("Cancel");
        HBox ccButtons = new HBox(confirmBox, cancelBox);

        VBox vbox = new VBox(contextLabel, countryBox, continentBox, confirmSpacer, ccButtons);
        vbox.setSpacing(10);

        Scene sc = new Scene(vbox);
        setResizable(false);
        setScene(sc);
    }

    private void setupButtonFunctionality() {
        confirmBox.setOnAction(e -> {
            ConfirmationBox cBox = new ConfirmationBox(2, continentInput.getText(), countryInput.getText(), "NULL");
        });
        cancelBox.setOnAction(e -> {
            DebugWindow dbgWin = new DebugWindow();
            close();
        });
    }

    private static String continentId;
    static void addToSQL() {
        Connection conn = Main.getMariaConn();

        try(Statement stmt = conn.createStatement()) {
            stmt.execute("USE globalinfo;");
            try {
                ResultSet continentConfirm = stmt.executeQuery("SELECT * FROM continent WHERE name=\"" + continentInput.getText() + "\";");
                continentConfirm.next();
                continentId = continentConfirm.getString("id");
            }
            catch(Exception e) {
                System.out.println("Continent input was invalid!");
                return;
            }
            System.out.println("Continent input returned valid!");

            stmt.execute("INSERT INTO country(name, continent_id) VALUES(" + countryInput.getText() + ", " + continentId + ");");
        }
        catch(Exception e) {
            System.out.println("idk this failed ig");
        }
    }
}
