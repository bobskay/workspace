#!/bin/sh
docker-compose stop
rm -rf *.jar
cp ../../../mservice/mservice-eureka-server/target/mservice-eureka-server-0.1.jar ./
docker-compose up -d