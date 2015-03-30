package org.labaix.logback;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Context;
import org.influxdb.dto.Serie;

import java.util.List;

/**
 * Created by nicolas on 15/03/15.
 */
public class InfluxDbConverter {

    public Serie toInflux(ILoggingEvent iLoggingEvent, InfluxDbSerie influxDbSerie, Context context) {

        PatternLayout patternLayout = new PatternLayout();
        patternLayout.setContext(context);
        patternLayout.setPattern(influxDbSerie.getName());
        patternLayout.start();

        String formattedSerieName = patternLayout.doLayout(iLoggingEvent);
        System.out.println("formattedSerieName="+formattedSerieName);

        Serie serie = new Serie.Builder(formattedSerieName)
                .columns(iLoggingEvent.getFormattedMessage().split(","))
                .values(iLoggingEvent.getArgumentArray())
                .build();
        System.out.println(serie);

        return serie;
    }
}
