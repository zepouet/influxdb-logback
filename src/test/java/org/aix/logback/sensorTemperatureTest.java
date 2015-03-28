package org.aix.logback;

import com.google.common.io.CharStreams;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerCertificates;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.messages.*;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by nicolas on 14/03/15.
 */
public class sensorTemperatureTest {

    private Random rand = new Random();
    private final int temperatureBetween(int min, int max) {
        return rand.nextInt(max-min+1)+min;
    }

    private static DockerClient docker = null;
    private InfluxDB influxDB;
    private static String boot2dockerIP;

    @BeforeClass
    public static void WarmUpTheEngine() {
        try {
            System.out.println("@BeforeClass - oneTimeSetUp");
            docker = DefaultDockerClient.fromEnv().build();

            List<Container> containers = docker.listContainers();
            for (Container container : containers) {
                if (container.image().contains("influxdb")) {
                    docker.killContainer(container.id());
                    docker.removeContainer(container.id());
                }
            }

            // Pull image influxdb
            //docker.pull("tutum/influxdb");

            // Create container with exposed ports
            final String[] ports = {"8083", "8086"};
            final ContainerConfig config = ContainerConfig.builder()
                    .image("tutum/influxdb:latest").exposedPorts(ports)
                    .build();

            // bind container ports to host ports
            final Map<String, List<PortBinding>> portBindings = new HashMap<String, List<PortBinding>>();
            for(String port : ports) {
                List<PortBinding> hostPorts = new ArrayList<PortBinding>();
                hostPorts.add(PortBinding.of("0.0.0.0", port));
                portBindings.put(port, hostPorts);
            }
            final HostConfig hostConfig = HostConfig.builder().portBindings(portBindings).build();

            final ContainerCreation creation = docker.createContainer(config);
            final String id = creation.id();
            System.out.println(id);

            // Inspect container
            final ContainerInfo info = docker.inspectContainer(id);
            System.out.println(info);

            // Start container
            docker.startContainer(id, hostConfig);

            Properties props = new Properties();
            InputStream is = ClassLoader.getSystemResourceAsStream("configuration.properties");
            props.load(is);
            boot2dockerIP = (String)props.get("boot2docker.ip");

        } catch(Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
        System.out.println("@AfterClass - oneTimeTearDown");
        docker.close();
    }

    @Before
    public void setUp() {
        System.out.println("@Before - setUp");
        try {
            // TODO : Ugly code -- Find a way with dockerExec to verify InfluxDB processus is right
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.influxDB = InfluxDBFactory.connect("http://"+boot2dockerIP+":8086", "root", "root");
        if (this.influxDB == null) {
            fail("cannot connect to influxdb");
        }
        this.influxDB.createDatabase("testdb");
    }

    @After
    public void tearDown() {
        System.out.println("@After - tearDown");
        this.influxDB.deleteDatabase("testdb");
    }

    @Test
    public void foo2() {
        assertTrue(true);
    }

    public void sensorTemperatureByMonth() {

        Logger logger = LoggerFactory.getLogger("sensorTemperature");
        int previousTempMin = 10;
        int previousTempMax = 30;
        int i = 0;
        while(i++<10) {
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


    static class LogStreamInputStream extends InputStream {

        private final LogStream logStream;
        private ByteBuffer currentBuffer = null;

        /**
         * @param logStream
         */
        public LogStreamInputStream(final LogStream logStream) {
            super();
            this.logStream = logStream;
        }

        @Override
        public int read() throws IOException {
            if (this.currentBuffer == null) {
                if (this.logStream.hasNext()) {
                    this.currentBuffer = this.logStream.next().content();
                }
            }

            int result;
            if (this.currentBuffer.remaining() > 0) {
                result = this.currentBuffer.get();
            } else {
                this.currentBuffer = this.logStream.next().content();
                result = this.currentBuffer.get();
            }
            return result;
        }
    }
}
