#!/usr/bin/env bash

INFRASTRUCTURE=infrastructure/docker/docker-compose.yml
ACCOUNT=domain/account-service/docker/docker-compose.yml

docker-compose \
    -f ${INFRASTRUCTURE} \
    -f ${ACCOUNT} \
    up
