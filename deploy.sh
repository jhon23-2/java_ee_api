#!/bin/bash

# Colors for output

YELLOW='\033[1;33m'
GREEN='\033[1;32m'
RED='\033[1;31m'
NC='\033[0m'


echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}  Auto Deploy Script for Java EE${NC}"
echo -e "${GREEN}========================================${NC}"

# Configuration
PROJECT_NAME="java-EE-1.0-SNAPSHOT"
WAR_FILE="./target/${PROJECT_NAME}.war"
DEPLOYMENTS_DIR="${WILDFLY_HOME}/standalone/deployments"
PORT_OFFSET=10

# Step 1: Stop WildFly server if it is running

echo -e "\n${YELLOW}[1/5] Stopping WildFly (if running)...${NC}"
pkill -f "standalone.sh" 2>/dev/null || echo "WildFly was not running"
sleep 2

# Step 2: Clean old deployment files if already is present

echo -e "\n${YELLOW}[2/5] Cleaning old deployment...${NC}"
rm -f ${DEPLOYMENTS_DIR}/${PROJECT_NAME}.war
rm -f ${DEPLOYMENTS_DIR}/${PROJECT_NAME}.war.deployed
rm -f ${DEPLOYMENTS_DIR}/${PROJECT_NAME}.war.failed
rm -f ${DEPLOYMENTS_DIR}/${PROJECT_NAME}.war.isdeploying

# Step 3: Build the project using Maven

echo -e "\n${YELLOW}[3/5] Building project with Maven...${NC}"
mvn clean package
if [ $? -ne 0 ]; then
    echo -e "${RED}Build failed! Exiting...${NC}"
    exit 1
fi


# Step 4: Copy the WAR file to WildFly deployments folder

echo -e "\n${YELLOW}[4/5] Copying WAR to deployments folder...${NC}"
cp ${WAR_FILE} ${DEPLOYMENTS_DIR}/
if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to copy WAR file! Exiting...${NC}"
    exit 1
fi


# Step 5: Start WildFly server

echo -e "\n${YELLOW}[5/5] Starting WildFly...${NC}"
cd ${WILDFLY_HOME}/bin
./standalone.sh -c standalone-full.xml -Djboss.socket.binding.port-offset=${PORT_OFFSET}


echo -e "\n${GREEN}========================================${NC}"
echo -e "${GREEN}  Deployment Complete!${NC}"
echo -e "${GREEN}========================================${NC}"
echo -e "Application will be available at:"
echo -e "${GREEN}http://localhost:$((8080 + PORT_OFFSET))/java-EE-1.0-SNAPSHOT/api/users${NC}"
echo -e "\nTo stop WildFly, run: ${YELLOW}pkill -f standalone.sh${NC}"
