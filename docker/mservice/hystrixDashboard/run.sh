#!/bin/sh
docker-compose stop
rm -rf *.jar
cp ../../../mservice/mservice-hystrix-dashboard/target/mservice-hystrix-dashboard-0.1.jar ./
docker-compose up -d