#!/bin/sh
docker-compose stop
rm -rf *.jar
cp ../../app/bookstoreService/bookstore-provider/target/bookstore-provider-0.1.jar ./
docker-compose up -d