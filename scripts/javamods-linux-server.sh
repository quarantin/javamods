#!/bin/bash

cd "$(dirname "${0}")"

JSON='javamods.json'

if [ -f './ProjectZomboid32' ]; then
	PZJSON='ProjectZomboid32.json'

elif [ -f './ProjectZomboid64' ]; then
	PZJSON='ProjectZomboid64.json'
fi

jq --tab '.vmArgs |= (. + ["-javaagent:java/javamods.jar"]) | .classpath |= (. + ["java/javassist.jar", "java/javamods.jar"])' "${PZJSON}" > "${JSON}"

./start-server.sh -pzexeconfig "${JSON}" "$@"
