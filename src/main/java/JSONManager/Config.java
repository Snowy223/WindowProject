package JSONManager;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class Config {
    private DatabaseConfig database;

    public static Config loadConfig(String filepath) throws IOException {
        ObjectMapper objMapper = new ObjectMapper();
        return objMapper.readValue(new File(filepath), Config.class);
    }

    public DatabaseConfig getDatabase() {
        return database;
    }
    public void setDatabase(DatabaseConfig database) {
        this.database = database;
    }
}
