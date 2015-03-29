package org.aix.logback;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.messages.*;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.InputStream;
import java.util.*;

import static org.junit.Assert.fail;

/**
 * Created by nicolas on 14/03/15.
 */
public class sensorTemperatureTest {

    private final Random rand = new Random();

    private int temperatureBetween(int min, int max) {
        return rand.nextInt(max - min + 1) + min;
    }

    private static DockerClient docker = null;
    private static String boot2dockerIP;
    private InfluxDB influxDB;

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
            for (String port : ports) {
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
            boot2dockerIP = (String) props.get("boot2docker.ip");

        } catch (Exception e) {
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
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("boot2dockerIP=" + boot2dockerIP);
        influxDB = InfluxDBFactory.connect("http://" + boot2dockerIP + ":8086", "root", "root");
        influxDB.createDatabase("testdb");
    }

    @After
    public void tearDown() {
        System.out.println("@After - tearDown");
        this.influxDB.deleteDatabase("testdb");
    }

    @Test
    public void sensorTemperatureByMonth() {
        Logger logger = LoggerFactory.getLogger("sensorTemperature");
        int i = 0;
        while (++i < 10) {
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
