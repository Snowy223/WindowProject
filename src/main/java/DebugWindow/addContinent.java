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
import java.sql.Statement;

public class addContinent extends Stage {
    private static TextField continentInput;
    private Button confirmBox, cancelBox;
    public addContinent() {
        setupContinentWindow();
        setupButtonFunctionality();
        show();
    }

    private void setupContinentWindow() {
        setTitle("Add continent");

        Label contextLabel       = new Label(DebugWindow.contextMenu("continent"));

        Label continentName      = new Label("Continent: ");
        continentInput           = new TextField();
        HBox continentBox        = new HBox(continentName, continentInput);

        Region confirmSpacer     = new Region();

        confirmBox               = new Button("Confirm");
        cancelBox                = new Button("Cancel");
        HBox ccButtons           = new HBox(confirmBox, cancelBox);

        confirmSpacer.setMinWidth(50);
        ccButtons.setSpacing(20);

        VBox vbox = new VBox(contextLabel, continentBox, confirmSpacer, ccButtons);
        vbox.setSpacing(10);

        Scene sc = new Scene(vbox);
        setResizable(false);
        setScene(sc);
    }

    private void setupButtonFunctionality() {
        confirmBox.setOnAction(e -> {
            ConfirmationBox cBox = new ConfirmationBox(1, continentInput.getText(), "NULL", "NULL");
        });
        cancelBox.setOnAction(e -> {
            DebugWindow dbgWin = new DebugWindow();
            close();
        });
    }

    static void addToSQL() {
        Connection conn = Main.getMariaConn();

        try(Statement stmt = conn.createStatement()) {
            stmt.execute("USE globalinfo;");
            stmt.execute("INSERT INTO continent(name) VALUES(" + continentInput.getText() + ");");
        }
        catch(Exception e) {
            System.out.println("idk this failed ig");
        }
    }
}
