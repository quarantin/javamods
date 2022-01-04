#!/bin/bash
cd "$(dirname "${0}")"

INSTDIR="${HOME}/.steam/steam/steamapps/common/ProjectZomboid/projectzomboid"

if "./jre64/bin/java" -version > /dev/null 2>&1; then
	ARCH="64"
	JAVAROOT="${INSTDIR}/jre64"

elif "./jre/bin/java" -version > /dev/null 2>&1; then
	ARCH="32"
	JAVAROOT="${INSTDIR}/jre"

else
	echo "couldn't determine 32/64 bit of java"
	exit 0
fi

echo "${ARCH}-bit java detected"
JAVA="${JAVAROOT}/bin/java"
MAIN="javamods.Main"
CLASSPATH=".:./*:$("${JAVA}" -classpath javamods.jar ${MAIN} -bootstrap)"
LD_PRELOAD="${LD_PRELOAD}:${JAVAROOT}/lib/libjsig.so"
LD_LIBRARY_PATH="${LD_LIBRARY_PATH}:../natives/linux${ARCH}:../natives:./linux${ARCH}"
XMODIFIERS= LD_PRELOAD="${LD_PRELOAD}" LD_LIBRARY_PATH="${LD_LIBRARY_PATH}" \
	"${INSTDIR}/ProjectZomboid${ARCH}"                      \
	-pzexejavacmd "${JAVA} -classpath ${CLASSPATH} ${MAIN} ${@}" \
	-pzexeconfig "./test-pzexe-linux-${ARCH}.json"          \
	"$@"
