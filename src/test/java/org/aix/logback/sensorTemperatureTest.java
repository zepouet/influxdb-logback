package org.aix.logback;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by nicolas on 14/03/15.
 */
public class sensorTemperatureTest {

    private Random rand = new Random();

    private final int temperatureBetween(int min, int max) {
        return rand.nextInt(max-min+1)+min;
    }

    @Test
    public void sensorTemperatureByMonth() {
        Logger logger = LoggerFactory.getLogger("sensorTemperature");
        int previousTempMin = 10;
        int previousTempMax = 30;
        int i = 0;
        while(true) {
            MDC.put("machine", "unit42");
            MDC.put("type", "assembly");
            int temperatureINT = temperatureBetween(19, 30);
            int temperatureEXT = temperatureBetween(5, 25);
            try {
                Thread.sleep(rand.nextInt(500));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("timestamp,external,internal", System.currentTimeMillis(), temperatureEXT, temperatureINT);
        }


    }


}
