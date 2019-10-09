#!/bin/sh
docker-compose stop
rm -rf *.jar
cp ../../../mservice/mservice-config-server/target/mservice-config-server-0.1.jar ./
docker-compose up -d