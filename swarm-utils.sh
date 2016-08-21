#!/usr/bin/env bash
set -e

NETWORK=skynet-network
SUBNET="10.0.9.0/24"
DISCOVERY_PORT=8761
MEMORY_LIMIT=1G

IMAGE_BASE_NAME=lingvo-movie

createInfrastructure(){
    createMySqlService
    createDiscovery
    createGateway
}

createAll(){
    createInfrastructure
    createService 'account-service'
    createService 'dictionary-service'
    createService 'webapp'
}

createSwarmMaster(){
 docker swarm init --advertise-addr "$1"
 isNetworkDefined=$(docker network ls | grep ${NETWORK})
    [ -z isNetworkDefined ] && {
        docker network create \
           --driver overlay \
           --subnet ${SUBNET} \
           ${NETWORK}
    }
}

createNetwork(){
 docker network create \
    --driver overlay \
    --subnet ${SUBNET} \
    ${NETWORK}
}

createMySqlService(){
   echo -n Creating MySql Service ' '
   docker service create \
       --name mysql \
       --replicas 1 \
       --network ${NETWORK} \
       --publish 33060:3306 \
       --env MYSQL_ROOT_PASSWORD=admin \
       --env MYSQL_DATABASE=lingvo-movie \
       mysql/mysql-server:5.7.12
}

createDiscovery(){
    echo -n  Creating Discovery Service ' '
    docker service create \
        --name discovery \
        --replicas 1 \
        --limit-memory ${MEMORY_LIMIT} \
        --publish ${DISCOVERY_PORT}:${DISCOVERY_PORT} \
        --network ${NETWORK} \
        ${IMAGE_BASE_NAME}/discovery
    postProcessor "$@"
}

createGateway(){
    echo -n Creating Gateway Service ' '
    docker service create \
        --name api-gateway \
        --replicas 1 \
        --limit-memory ${MEMORY_LIMIT} \
        --constraint 'node.role == manager' \
        --publish 80:8080 \
        --network ${NETWORK} \
        ${IMAGE_BASE_NAME}/api-gateway
    postProcessor "$@"
}

createService(){
    echo -n Creating $1 ' '
    docker service create \
        --name $1 \
        --replicas 1 \
        --limit-memory ${MEMORY_LIMIT} \
        --network ${NETWORK} \
        ${IMAGE_BASE_NAME}/$1
    postProcessor "$@"
}

logs(){
    [[ $1 == 'docker' ]] &&  sudo tail -n 200 -f  "/var/log/upstart/docker.log"
    local container=$(docker ps --filter name=${1} --format {{.ID}})
    [ -z $container ] && sleep 1 && logs "$@"
    docker logs -f $(docker ps --filter name=${1} --format {{.ID}})
}

stats(){
    docker stats $(docker ps --format={{.Names}})
}

stop(){
    docker service rm $(docker service ls --quiet)
}

cleanup(){
    docker rmi $(docker images --filter "dangling=true" -q --no-trunc)
}

cleanupC(){
    docker rm $(docker ps -a --quiet)
}

login(){
    docker exec -it $(docker ps --filter name=${1} --quiet) /bin/sh
}

postProcessor(){
    local serviceName=$1
    for var in "$@"
    do
	    if [ "$var" = '--logs' -o "$var" = '--log' ]; then
		    logs ${serviceName}
	    fi
    done
}


# *************** Aliases **************************

infrastructure(){
    createInfrastructure
}

all(){
    createAll
}

start(){
    createAll
}

master(){
    createSwarmMaster $1
}

network(){
 createNetwork
}

mysql(){
   createMySqlService
}

discovery(){
    createDiscovery "$@"
}

gateway(){
    createGateway "$@"
}

service(){
    createService "$@"
}


# call arguments verbatim:
"$@"



