package WindowProject;

import javafx.scene.control.ComboBox;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/*
The structure of this file used to be in Main.java, but I moved it here because it's long and difficult to read when
nested in so much extra code surrounding it. This class exists solely for grabbing city data from all 7 supplied
continents (including Antarctica), as long as all 7 Continents exist on the MariaDB database, this function will work
just fine.
*/

public class CityListLoading {
    public void load7Continents(Connection conn, ComboBox<String> cityComboBox) {
        int i = 0;
        String[] continentList = { "North America", "South America", "Europe", "Africa", "Asia", "Oceania", "Antarctica" };
        String getCities = "SELECT city.name FROM city " +
                           "JOIN country ON city.country_id = country.id " +
                           "JOIN continent ON country.continent_id = continent.id " +
                           "WHERE continent.name = '";
        try(Statement stmt = conn.createStatement()) {
            stmt.execute("USE globalinfo;");
            for(i = 0; i < 7; i++) {
                ResultSet CitiesList = stmt.executeQuery(getCities + continentList[i] + "';");
                while(CitiesList.next()) {
                    String city = CitiesList.getString("name");
                    cityComboBox.getItems().add(city);
                }
            }
        }
        catch(Exception e) {
            System.out.println("Failed to get city column from table " + continentList[i]);
        }
    }
}
