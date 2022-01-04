#!/bin/bash
set -e

JAR=javamods.jar
ZOMBOID=${HOME}/.steam/steam/steamapps/common/ProjectZomboid/projectzomboid/

javac --release 8 -cp "${ZOMBOID}" javamods/*.java javamods/mod/*.java
jar -cf "${JAR}" javamods/*.class javamods/mod/*.class
