package org.labaix.logback;

/**
 * Created by nicolas on 29/03/15.
 */
public class InfluxDbSource {

    private String ip;
    private String port;
    private String database;
    private String user;
    private String password;
    private String create;

    public String getCreate() {
        return this.create;
    }

    public void setCreate(String create) {
        this.create = create;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "InfluxDbSource{" +
                "ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", database='" + database + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", create='" + create + '\'' +
                '}';
    }

    public String getPort() {
        return this.port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabase() {
        return this.database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InfluxDbSource)) return false;

        InfluxDbSource that = (InfluxDbSource) o;

        if (this.ip != null ? !this.ip.equals(that.ip) : that.ip != null) return false;
        if (this.port != null ? !this.port.equals(that.port) : that.port != null) return false;
        if (this.database != null ? !this.database.equals(that.database) : that.database != null) return false;
        if (this.user != null ? !this.user.equals(that.user) : that.user != null) return false;
        if (this.password != null ? !this.password.equals(that.password) : that.password != null) return false;
        return !(this.create != null ? !this.create.equals(that.create) : that.create != null);

    }

    @Override
    public int hashCode() {
        int result = this.ip != null ? this.ip.hashCode() : 0;
        result = 31 * result + (this.port != null ? this.port.hashCode() : 0);
        result = 31 * result + (this.database != null ? this.database.hashCode() : 0);
        result = 31 * result + (this.user != null ? this.user.hashCode() : 0);
        result = 31 * result + (this.password != null ? this.password.hashCode() : 0);
        result = 31 * result + (this.create != null ? this.create.hashCode() : 0);
        return result;
    }
}
