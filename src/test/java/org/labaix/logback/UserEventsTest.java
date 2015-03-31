package org.labaix.logback;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Database;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.fail;

/**
 * Created by nicolas on 29/03/15.
 */
public class UserEventsTest {

    private final Random rand = new Random();
    private final Logger loggerXXX = LoggerFactory.getLogger(UserEventsTest.class);
    private static String boot2dockerIP;
    private static InfluxDB influxDB;

    @BeforeClass
    public static void WarmUpTheEngine() {
        try {
            Properties props = new Properties();
            InputStream is = ClassLoader.getSystemResourceAsStream("configuration.properties");
            props.load(is);
            boot2dockerIP = (String) props.get("boot2docker.ip");

            System.out.println("boot2docker.ip=" + boot2dockerIP);
            influxDB = InfluxDBFactory.connect("http://" + boot2dockerIP + ":8086", "root", "root");
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Before
    public void setUp() {
        List<Database> databases = this.influxDB.describeDatabases();
        boolean found  = databases.stream().filter(o -> o.getName().equalsIgnoreCase("userdb")).findFirst().isPresent();
        if (!found) {
            this.influxDB.createDatabase("userdb");
        }
        System.out.println("@Before - setUp");
    }

    @After
    public void tearDown() {
        System.out.println("@After - tearDown");
        //this.influxDB.deleteDatabase("userdb");
    }

    @Test
    public void concurrenceBetweenTwoUsers() {
        Logger logger = LoggerFactory.getLogger("UserEvents");
        int i = 0;
        //while(i++<10)
        //    logger.info("code,value,controller_action", 200+i, 234+i, "users#show");

        List<Runnable> runnables = new ArrayList<Runnable>();

        runnables.add(new MonRunnable());
        runnables.add(new MonRunnable());
        runnables.add(new MonRunnable());
        runnables.add(new MonRunnable());

        //Cette fois on créer un pool de 10 threads maximum
        ExecutorService execute = Executors.newFixedThreadPool(4);

        executeRunnables(execute, runnables);
    }

    public static void executeRunnables(final ExecutorService service, List<Runnable> runnables){
        //On exécute chaque "Runnable" de la liste "runnables"
        for(Runnable r : runnables){
            service.execute(r);
        }
        service.shutdown();
    }


    class MonRunnable implements Runnable {
        @Override
        public void run() {

            try {
                int i = 0;
                while(i++ < 10) {
                    Thread.currentThread().sleep(23);
                    //System.out.println("lol");
                    loggerXXX.info("thread, code,value,controller_action", Thread.currentThread().getName(), 200 + i, 234 + i, "users#show");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
