package org.aix.logback;

import org.slf4j.MDC;

/**
 * Created by nicolas on 15/03/15.
 */
public class SerieConfig {

    private String database;

    @Override
    public String toString() {
        return "SerieConfig{" +
                "database='" + database + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getDatabase() {
        return this.database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
