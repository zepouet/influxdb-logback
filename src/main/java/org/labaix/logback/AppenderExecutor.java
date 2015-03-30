package org.labaix.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Context;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Serie;

import java.util.List;
import java.util.Map;

/**
 * Converts a log event into a compatible object for api influxdb
 */
public class AppenderExecutor {

    private final InfluxDbConverter influxDbConverter;
    private final InfluxDbSerie influxDbSerie;
    private final InfluxDB influxDB;
    private final Context context;
    private final InfluxDbSource source;

    public AppenderExecutor(InfluxDbConverter influxDbConverter, InfluxDbSource source,
                            InfluxDbSerie influxDbSerie, InfluxDB influxDB, Context context) {
        this.influxDbConverter = influxDbConverter;
        this.influxDbSerie = influxDbSerie;
        this.source = source;
        this.influxDB = influxDB;
        this.context = context;
    }

    /**
     * The main append method. Takes the event that is being logged, formats if for InfluxDB and then sends it over the wire
     * to the metrics endpoints
     *
     * @param logEvent The event that we are logging
     */
    public void append(final ILoggingEvent logEvent) {
        String loggerName = logEvent.getLoggerName();
        //InfluxDbSerie influxDbSerie = influxDbSeries.stream().filter(o -> o.getId().equalsIgnoreCase(loggerName)).findFirst().get();
        Serie serie = influxDbConverter.toInflux(logEvent, influxDbSerie, context);
        influxDB.write(this.source.getDatabase(), this.influxDbSerie.getRawTimeUnit(), serie);
    }

}
