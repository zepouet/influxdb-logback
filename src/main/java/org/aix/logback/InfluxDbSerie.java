package org.aix.logback;

import java.util.concurrent.TimeUnit;

/**
 * Created by nicolas on 15/03/15.
 */
public class InfluxDbSerie {

    private String name;
    private String version;
    private TimeUnit timeUnit;

    public TimeUnit getTimeUnit() {
        return this.timeUnit;
    }

    public void setTimeUnit(String name) {
        this.timeUnit = TimeUnit.valueOf(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InfluxDbSerie)) return false;

        InfluxDbSerie that = (InfluxDbSerie) o;

        if (this.name != null ? !this.name.equals(that.name) : that.name != null) return false;
        if (this.version != null ? !this.version.equals(that.version) : that.version != null) return false;
        return !(this.timeUnit != null ? !this.timeUnit.equals(that.timeUnit) : that.timeUnit != null);

    }

    @Override
    public int hashCode() {
        int result = this.name != null ? this.name.hashCode() : 0;
        result = 31 * result + (this.version != null ? this.version.hashCode() : 0);
        result = 31 * result + (this.timeUnit != null ? this.timeUnit.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Serie{" +
                "name='" + name + '\'' +
                ", timeUnit='" + timeUnit + '\'' +
                ", version='" + version + '\'' +
                '}';
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
