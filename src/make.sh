#!/bin/bash
set -e

JAR=javamods.jar
ZOMBOID=${HOME}/.steam/steam/steamapps/common/ProjectZomboid/projectzomboid/

javac --release 8 -cp "${ZOMBOID}" zombie/javamods/*.java zombie/javamods/mod/*.java
jar -cf "${JAR}" zombie/javamods/*.class zombie/javamods/mod/*.class
