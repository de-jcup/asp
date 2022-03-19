#!/bin/bash 


RED='\033[31m'
WHITE='\033[97m'
NC='\033[0m'
BOLD='\033[1m'
GREEN='\033[92m'
DIM='\033[2m'
STOP='\033[0m'

function usage {
    echo -e "$BOLD"
    echo -e "Usage:$NC"
    echo -e "$0 <signing-password>"
    echo -e "${GREEN}Necessary Gradle properties:$NC e.g. from ~/.gradle/gradle.properties or ORG_GRADLE_PROJECT_* properties"
    echo -e "${RED}signing.keyId$NC=12345678"
    echo -e "${RED}signing.secretKeyRingFile$NC=~/.gnupg/secring.gpg"
    echo ""
    echo -e "${GREEN}Necessary Environment entries:$NC"
    echo -e "${RED}OSSRH_USERNAME$NC:sonatype user name"
    echo -e "${RED}OSSRH_PASSWORD$NC:sonatype password or token"
    echo -e "${RED}SIGNING_PASSWORD$NC:signing password"
   
}

# check environment set
if [ -z "$OSSRH_USERNAME" ]; then
    echo -e "Please export ${RED}OSSRH_USERNAME${NC}"
    usage
    exit 1;
fi

if [ -z "$OSSRH_PASSWORD" ]; then
    echo -e "Please export ${RED}OSSRH_PASSWORD${NC}"
    usage
    exit 1;
fi

if [ -z "$SIGNING_PASSWORD" ]; then
    echo -e "Please export ${RED}SIGNING_PASSWORD${NC}"
    usage
    exit 1;
fi

./gradlew clean build buildDist uploadArchives -Psigning.password=$SIGNING_PASSWORD
