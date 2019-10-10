#!/bin/sh
docker-compose stop
rm -rf *.jar
cp ../../../mservice/mservice-admin/target/mservice-admin-0.1.jar ./
docker-compose up -d