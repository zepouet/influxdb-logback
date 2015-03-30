package org.labaix.logback;

import java.util.concurrent.TimeUnit;

/**
 * Created by nicolas on 15/03/15.
 */
public class InfluxDbSerie {

    private String id;
    private String name;
    private String timeUnit;

    public String getTimeUnit() {
        return this.timeUnit;
    }

    public void setTimeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
    }

    public TimeUnit getRawTimeUnit() {
        return TimeUnit.valueOf(timeUnit);
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InfluxDbSerie)) return false;

        InfluxDbSerie that = (InfluxDbSerie) o;

        if (!this.id.equals(that.id)) return false;
        if (!this.name.equals(that.name)) return false;
        return timeUnit.equals(that.timeUnit);

    }

    @Override
    public int hashCode() {
        int result = this.id.hashCode();
        result = 31 * result + this.name.hashCode();
        result = 31 * result + this.timeUnit.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "InfluxDbSerie{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", timeUnit='" + timeUnit + '\'' +
                '}';
    }
}
