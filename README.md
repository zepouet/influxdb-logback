INFLUXDB-LOGBACK
================

An Influx Appender for Logback
------------------------------

Use this appender to log messages with logback to a influxdb server.

If you don't know what [InfluxDB](http://influxdb.org) is, jump on the band wagon!

Installation
-----------------------------------

Simply add logback-gelf to your classpath. Either
[download the jar](https://github.com/Moocar/logback-gelf/downloads)
or if you're in [maven](http://mvnrepository.com/artifact/me.moocar/logback-gelf) land, the dependency details are below.

        <dependencies>
            ...
            <dependency>
                <groupId>org.aix.logback</groupId>
                <artifactId>influxdb-logback</artifactId>
                <version>0.42</version>
            </dependency>
            ...
        </dependencies>

Configuring Logback
---------------------

Add the following to your logback.xml configuration file.

[src/main/resources/logback.xml](https://github.com/zepouet/influxdb-logback/blob/master/src/test/resources/logback.xml)

    <configuration>
        ...
        <appender name="CUSTOMER_EVENTS" class="org.aix.logback.InfluxDbAppender">
            <influxDbUrl>http://192.168.59.103</influxDbUrl>
            <influxDbPort>8086</influxDbPort>
            <influxDbLogin>root</influxDbLogin>
            <influxDbPassword>root</influxDbPassword>
            <serieConfig>
                <database>aTimeSeries</database>
                <name>customer_events_${byDay}</name>
                <columns>time,customerId,type</columns>
             </serieConfig>
        </appender>

        <logger name="CustomerEvents" >
            <appender-ref ref="CUSTOMER_EVENTS" />
        </logger>
        ...
    </configuration>

Properties
----------

*   **influxDbUrl**: The hostname of the influxdb server to send messages to. Defaults to "localhost"
*   **influxDbPort**: The port of the influxdb server to send messages to. Defaults to "8086"
*   **influxDbLogin**: The login of the account to send messages to. Defaults to "root"
*   **influxDbLogin**: The password of the account to send messages to. Defaults to "root"

