#!/bin/bash 

username=$1
bintraykey=$2
passphrase=$3

RED='\033[0;31m'
WHITE='\033[97m'
NC='\033[0m'
BOLD='\033[1m'
GREEN='\033[92m'
DIM='\033[2m'
STOP='\033[0m'

function usage {
    echo -e "$BOLD"
    echo -e "Usage:$NC"
    echo -e "$0 <ossrhUsername> <ossrhPassword> <gpg<gpg-secretKeyRingfile> <gpg-passphrase>"
}

if [ $# -lt 3 ]; then
    usage
    exit 1
fi
#./gradlew buildDist 
./gradlew clean buildDist uploadArchives