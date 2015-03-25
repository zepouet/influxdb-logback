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


Additional Fields
-----------------

Additional Fields can be added very easily. Let's take an example of adding the ip address of the client to every logged
message. To do this we add the ip address as a key/value to the [slf4j MDC](http://logback.qos.ch/manual/mdc.html)
(Mapped Diagnostic Context) so that the information persists for the length of the request, and then we inform
logback-gelf to look out for this mapping every time a message is logged.

1.  Store IP address in MDC

        // Somewhere in server code that wraps every request
        ...
        org.slf4j.MDC.put("ipAddress", getClientIpAddress());
        ...

2.  Inform logback-gelf of MDC mapping

        ...
        <appender name="GELF" class="me.moocar.logbackgelf.GelfAppender">
            ...
            <additionalField>ipAddress:_ip_address</additionalField>
            ...
        </appender>
        ...

The syntax for the additionalFields in logback.groovy is the following

    additionalFields = [<MDC Key>:<GELF Additional field name>, ...]

where `<MDC Key>` is unquoted and `<GELF Additional field name>` is quoted. It should also begin with an underscore (GELF standard)

If the property `includeFullMDC` is set to true, all fields from the MDC will be added to the gelf message. Any key, which is not
listed as `additionalField` will be prefixed with an underscore. Otherwise the field name will be obtained from the 
corresponding `additionalField` mapping.

If the property `includeFullMDC` is set to false (default value) then only the keys listed as `additionalField` will be 
added to a gelf message.

Static Additional Fields
-----------------

Use static additional fields when you want to add a static key value pair to every GELF message. Key is the additional
field key (and should thus begin with an underscore). The value is a static string.

E.g in the appender configuration:

        <appender name="GELF" class="me.moocar.logbackgelf.GelfAppender">
            ...
            <staticAdditionalField>_node_name:www013</staticAdditionalField>
            ...
        </appender>
        ...

Field type conversion
-----------------

You can configure a specific field to be converted to a numeric type. Key is the additional field key (and should thus
begin with an underscore), value is the type to convert to. Currently supported types are ``int``, ``long``, ``float``
and ``double``.

        <appender name="GELF" class="me.moocar.logbackgelf.GelfAppender">
            ...
            <fieldType>_request_id:long</fieldType>
            ...
        </appender>
        ...

logback-gelf will leave the field value alone (i.e.: send it as String) and print the stacktrace if the conversion fails.


Change Log
--------------------------------------

* Development version 0.13-SNAPSHOT (current Git `master`)
* Release [0.12] on 2014-Nov-04
  * Explicitly set Zipper string encoding to UTF-8 [#41](../../issues/41)
* Release [0.11] on 2014-May-18
  * Added field type conversion [#30](../../issues/30)
  * Use FQDN or fallback to hostname [#32](../../issues/32)
  * Update dependencies [#36](../../issues/36)
  * Remove copyright notice on InternetUtils [#38](../../issues/38)
  * Better testing of line and file in exceptions [#34](../../issues/34)
* Release [0.10p2] on 2014-Jan-12
  * Added hostName property [#28](../../issues/28)
  * Reverted Windows timeout [#29](../../issues/29)