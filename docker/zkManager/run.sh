#!/bin/sh
rm -rf *.jar
cp ../../app/zk-manager/target/zk-manager-0.1.jar ./
docker-compose up -d