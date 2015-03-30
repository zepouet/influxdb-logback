package org.labaix.logback;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${USER} on ${DATE}.
 */
public class InfluxDbAppender extends AppenderBase<ILoggingEvent> {

    private AppenderExecutor appenderExecutor;
    private InfluxDbSerie serie;
    private InfluxDbSource source;

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        appenderExecutor.append(iLoggingEvent);
    }

    @Override
    public void start() {
        super.start();
        initExecutor();
    }

    public InfluxDbSerie getSerie() {
        return this.serie;
    }

    public void setSerie(InfluxDbSerie serie) {
        this.serie = serie;
    }

    public AppenderExecutor getAppenderExecutor() {
        return this.appenderExecutor;
    }

    public void setAppenderExecutor(AppenderExecutor appenderExecutor) {
        this.appenderExecutor = appenderExecutor;
    }

    public InfluxDbSource getSource() {
        return this.source;
    }

    public void setSource(InfluxDbSource source) {
        this.source = source;
    }

    /**
     * This is an ad-hoc dependency injection mechanism. We don't want create all these classes every time a message is
     * logged. They will hang around for the lifetime of the appender.
     */
    private void initExecutor() {
        System.out.println(":: initExecutor :: begin");
        InfluxDB influxDB = InfluxDBFactory.connect("http://"+ source.getIp() + ":" + source.getPort()
                , source.getUser(), source.getPassword());

        InfluxDbConverter converter = new InfluxDbConverter();
        appenderExecutor = new AppenderExecutor(converter, source, serie, influxDB, getContext());

        String database = this.source.getDatabase();
        if ("true".equalsIgnoreCase(this.source.getCreate())) {
            List<Database> databases = influxDB.describeDatabases();
            boolean found  = databases.stream().filter(o -> o.getName().equalsIgnoreCase(database)).findFirst().isPresent();
            if (!found) {
                influxDB.createDatabase(database);
            }
            System.out.println("create database : " + this.source.getDatabase());
        }

        System.out.println(":: initExecutor :: end");
    }

}
