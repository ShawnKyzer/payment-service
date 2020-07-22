#!/bin/bash

cd delivery
docker-compose down
docker-compose build
docker-compose up
