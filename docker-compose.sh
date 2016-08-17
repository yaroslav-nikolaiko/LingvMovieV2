#!/usr/bin/env bash

INFRASTRUCTURE=infrastructure/docker/docker-compose.yml
ACCOUNT=domain/account-service/docker/docker-compose.yml
DICTIONARY=domain/dictionary-service/docker/docker-compose.yml
WEBAPP=domain/webapp/docker/docker-compose.yml

docker-compose \
    -f ${INFRASTRUCTURE} \
    -f ${ACCOUNT} \
    -f ${DICTIONARY} \
    -f ${WEBAPP} \
    up
