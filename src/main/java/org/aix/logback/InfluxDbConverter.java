package org.aix.logback;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Context;
import org.influxdb.dto.Serie;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by nicolas on 15/03/15.
 */
public class InfluxDbConverter {

    public Serie toInflux(ILoggingEvent iLoggingEvent, SerieConfig serieConfig, Context context) {

        String serieName = serieConfig.getName();

        PatternLayout patternLayout = new PatternLayout();
        patternLayout.setContext(context);
        patternLayout.setPattern(serieName);
        patternLayout.start();
        String formattedSerieName = patternLayout.doLayout(iLoggingEvent);

        Serie serie = new Serie.Builder(formattedSerieName)
                .columns(iLoggingEvent.getFormattedMessage().split(","))
                .values(iLoggingEvent.getArgumentArray())
                .build();
        System.out.println(serie);
        return serie;
    }
}
