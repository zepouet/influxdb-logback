INFLUXDB-LOGBACK
================

An InfluxDB Appender for Logback
--------------------------------

Use this appender to log messages with logback to a influxdb server.

If you don't know what [InfluxDB](http://influxdb.org) is, jump on the band wagon!

Installation
-----------------------------------

Simply add logback-gelf to your classpath. Either
[download the jar](https://github.com/zepouet/influxdb-logback/downloads)
or if you're in [maven](http://mvnrepository.com/artifact/zepouet/influxdb-logback) land, the dependency details are below.

        <dependencies>
            ...
            <dependency>
                <groupId>org.labaix.logback</groupId>
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
       <appender name="SENSOR_TEMPERATURE" class="org.aix.logback.InfluxDbAppender">
               <source>
                   <ip>192.168.59.103</ip>
                   <port>8086</port>
                   <user>root</user>
                   <password>root</password>
                   <database>testdb</database>
                   <create>true</create>
               </source>
               <serie>
                   <version>0.8</version>
                   <name>machine.%X{machine}.type.%X{type} temperature_${byMonth}</name>
                   <timeUnit>MILLISECONDS</timeUnit>
               </serie>
           </appender>
       
           <logger name="SensorTemperature" level="info">
               <appender-ref ref="SENSOR_TEMPERATURE"/>
           </logger>
        ...
    </configuration>

Properties
----------

*   **source/ip**: The ip or hostname of the influxdb server to send messages to. Defaults to "localhost"
*   **source/port**: The port of the influxdb server to send messages to. Defaults to "8086"
*   **source/user**: The login of the account to send messages to. Defaults to "root"
*   **source/password**: The password of the account to send messages to. Defaults to "root"


Additional Fields
-----------------

Additional Fields can be added very easily. Let's take an example of adding the ip address of the client to every logged
message. To do this we add the ip address as a key/value to the [slf4j MDC](http://logback.qos.ch/manual/mdc.html)
(Mapped Diagnostic Context) so that the information persists for the length of the request, and then we inform
logback-influxdb to look out for this mapping every time a message is logged.

1.  Store field in MDC and exploit it.

        // Somewhere in server code that wraps every request
        // we catch the ip address and add it into context
        ...
        org.slf4j.MDC.put("ipAddress", getClientIpAddress());
        ...
        
        So you could use %X{ipAddress} into serie name pattern.

2.  Gather information form environment variable (TODO)

        ...
        <appender name="INFLUXDB" class="org.aix.logback.InfluxDbAppender">
            ...
            <additionalField>serverName.${env.serverName}</additionalField>
            ...
        </appender>
        ...


Roadmap
--------------------------------------

TODO