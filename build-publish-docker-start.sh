#!/bin/bash

./gradlew clean build
gradle clean build

yes | cp -rf build/libs/technical-test-payment-service.jar delivery/TechnicalTestPaymentService/bin

sh start-docker.sh