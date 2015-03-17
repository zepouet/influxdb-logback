package org.aix.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Serie;

import java.util.concurrent.TimeUnit;

/**
 * Converts a log event into a compatible object for api influxdb
 */
public class AppenderExecutor {

    private final InfluxDbConverter influxDbConverter;
    private final SerieConfig serieConfig;
    private final InfluxDB influxDB;

    public AppenderExecutor(InfluxDbConverter influxDbConverter, SerieConfig serieConfig, InfluxDB influxDB) {
        this.influxDbConverter = influxDbConverter;
        this.serieConfig = serieConfig;
        this.influxDB = influxDB;
    }

    /**
      * The main append method. Takes the event that is being logged, formats if for InfluxDB and then sends it over the wire
      * to the metrics endpoints
      *
      * @param logEvent The event that we are logging
    */
    public void append(final ILoggingEvent logEvent) {
        Serie serie = influxDbConverter.toInflux(logEvent, serieConfig);
        influxDB.write(serieConfig.getDatabase(), TimeUnit.MILLISECONDS, serie);
    }

}
