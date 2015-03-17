package org.aix.logback;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by nicolas on 14/03/15.
 */
public class IntegrationTest {

    @Test
    public void test1() {
        Logger logger = LoggerFactory.getLogger("CustomerEvents");
        int i = 0;
        while(++i<10) {
            logger.info("{} {} {}", System.currentTimeMillis(), "nicolas muller", "achat");
        }
    }

}
