#!/bin/bash

./gradlew clean build
gradle clean build

yes | cp -rf build/libs/payment-service.jar delivery/PaymentService/bin

sh start-docker.sh