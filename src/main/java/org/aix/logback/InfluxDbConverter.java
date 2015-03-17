package org.aix.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.influxdb.dto.Serie;

/**
 * Created by nicolas on 15/03/15.
 */
public class InfluxDbConverter {

    public Serie toInflux(ILoggingEvent iLoggingEvent, SerieConfig serieConfig) {
        String serieName = serieConfig.getName();
        Serie serie = new Serie.Builder(serieName)
                .columns(serieConfig.getColumns().split(","))
                .values(iLoggingEvent.getArgumentArray())
                .build();
        return serie;
    }
}
