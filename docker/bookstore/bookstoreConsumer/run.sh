#!/bin/sh
docker-compose stop
rm -rf *.jar
cp ../../../app/bookstoreService/bookstore-consumer/target/bookstore-consumer-0.1.jar ./
docker-compose up -d