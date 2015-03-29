package org.aix.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;

/**
 * Created by ${USER} on ${DATE}.
 */
public class InfluxDbAppender extends AppenderBase<ILoggingEvent> {

    private AppenderExecutor appenderExecutor;

    private String influxDbUrl;
    private String influxDbPort;
    private String influxDbLogin;
    private String influxDbPassword;
    private SerieConfig serieConfig;

    public void setSerieConfig(SerieConfig serieConfig) {
        this.serieConfig = serieConfig;
    }

    @Override
    public String toString() {
        return "InfluxdbAppender{" +
                "serieConfig=" + serieConfig +
                ", influxDbUrl='" + influxDbUrl + '\'' +
                ", influxDbPort='" + influxDbPort + '\'' +
                ", influxDbLogin='" + influxDbLogin + '\'' +
                ", influxDbPassword='" + influxDbPassword + '\'' +
                '}';
    }

    public void setInfluxDbUrl(String influxDbUrl) {
        this.influxDbUrl = influxDbUrl;
    }

    public void setInfluxDbPort(String influxDbPort) {
        this.influxDbPort = influxDbPort;
    }

    public void setInfluxDbLogin(String influxDbLogin) {
        this.influxDbLogin = influxDbLogin;
    }

    public void setInfluxDbPassword(String influxDbPassword) {
        this.influxDbPassword = influxDbPassword;
    }

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        appenderExecutor.append(iLoggingEvent);
    }

    @Override
    public void start() {
        super.start();
        initExecutor();
    }

    /**
     * This is an ad-hoc dependency injection mechanism. We don't want create all these classes every time a message is
     * logged. They will hang around for the lifetime of the appender.
     */
    private void initExecutor() {
        System.out.println(":: initExecutor :: begin");
        System.out.println(toString());

        InfluxDB influxDB = InfluxDBFactory.connect(influxDbUrl + ":" + influxDbPort, influxDbLogin, influxDbPassword);

        InfluxDbConverter converter = new InfluxDbConverter();
        appenderExecutor = new AppenderExecutor(converter, serieConfig, influxDB, getContext());
        System.out.println(":: initExecutor :: end");

    }


}
