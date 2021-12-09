#!/bin/bash

INSTDIR="${HOME}/.steam/steam/steamapps/common/ProjectZomboid/projectzomboid"
SCRIPTDIR="$(dirname "${0}")"

if "${INSTDIR}/jre64/bin/java" -version > /dev/null 2>&1; then
	ARCH="64"
	JAVAROOT="${INSTDIR}/jre64"

elif "${INSTDIR}/jre/bin/java" -version > /dev/null 2>&1; then
	ARCH="32"
	JAVAROOT="${INSTDIR}/jre"

else
	echo "couldn't determine 32/64 bit of java"
	exit 0
fi

echo "${ARCH}-bit java detected"
MAIN="zombie.javamods.MainJavaMods"
CLASSPATH="${INSTDIR}:${INSTDIR}/*:${INSTDIR}/JavaMods/*:${SCRIPTDIR}/*:${SCRIPTDIR}/JavaMods/*"
export LD_LIBRARY_PATH="${LD_LIBRARY_PATH}:../natives/linux${ARCH}:../natives:${INSTDIR}/linux${ARCH}"
XMODIFIERS= LD_PRELOAD="${LD_PRELOAD}:${JAVAROOT}/lib/libjsig.so"        \
	"${INSTDIR}/ProjectZomboid${ARCH}"                                   \
	-pzexejavacmd "${JAVAROOT}/bin/java -classpath ${CLASSPATH} ${MAIN}" \
	-pzexeconfig "${INSTDIR}/test-pzexe-linux-${ARCH}.json"              \
	"$@"
