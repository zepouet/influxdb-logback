#!/bin/bash

docker ps -a | grep influxdb | awk '{print $1}' | xargs docker kill
docker ps -a | grep influxdb | awk '{print $1}' | xargs docker rm

echo "###############################"
echo "# CREATE CONTAINER : INFLUXDB #"
echo "###############################"

docker run -p 8086:8086 -p 8083:8083 -d tutum/influxdb:latest

# sleep needed for influxdb init
# TODO : use docker exec to test the start for the processus influxdb
sleep 5
