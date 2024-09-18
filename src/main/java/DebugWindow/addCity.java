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

public class addCity extends Stage {
    private static TextField cityInput;
    private static TextField countryInput;
    private Button confirmBox, cancelBox;

    public addCity() {
        setupCityWindow();
        setupButtonFunctionality();
        show();
    }

    private void setupCityWindow() {
        setTitle("Add city");

        Label contextLabel = new Label(DebugWindow.contextMenu("city"));

        Label cityName = new Label("City: ");
        cityInput = new TextField();
        HBox cityBox = new HBox(cityName, cityInput);

        Label countryName = new Label("Country of city: ");
        countryInput = new TextField();
        HBox countryBox = new HBox(countryName, countryInput);

        Region confirmSpacer = new Region();
        confirmSpacer.setMinWidth(50);

        confirmBox = new Button("Confirm");
        cancelBox = new Button("Cancel");
        HBox ccButtons = new HBox(confirmBox, cancelBox);

        VBox vbox = new VBox(contextLabel, cityBox, countryBox, confirmSpacer, ccButtons);
        vbox.setSpacing(10);

        Scene sc = new Scene(vbox);
        setResizable(false);
        setScene(sc);
    }

    private void setupButtonFunctionality() {
        confirmBox.setOnAction(e -> {
            ConfirmationBox cBox = new ConfirmationBox(3, "NULL", countryInput.getText(), cityInput.getText());
        });
        cancelBox.setOnAction(e -> {
            DebugWindow dbgWin = new DebugWindow();
            close();
        });
    }

    static String countryId;
    static void addToSQL() {
        Connection conn = Main.getMariaConn();

        try(Statement stmt = conn.createStatement()) {
            stmt.execute("USE globalinfo;");
            try {
                ResultSet countryConfirm = stmt.executeQuery("SELECT * FROM country WHERE name=\"" + countryInput.getText() + "\";");
                countryConfirm.next();
                countryId = countryConfirm.getString("id");
            }
            catch(Exception e) {
                System.out.println("Country input was invalid!");
                return;
            }
            System.out.println("Country input returned valid!");

            stmt.execute("INSERT INTO city(name, country_id) VALUES(\"" + cityInput.getText() + "\", " + countryId + ");");
        }
        catch(Exception e) {
            System.out.println("idk this failed ig");
        }
    }
}
