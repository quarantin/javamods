#!/bin/bash
cd "$(dirname "${0}")"

INSTDIR="$(pwd)"

if "./jre64/bin/java" -version > /dev/null 2>&1; then
	ARCH="64"
	ARCHNAME="amd64"
	JAVAROOT="${INSTDIR}/jre64"
	DEFAULT_RAM="2048m"

elif "./jre/bin/java" -version > /dev/null 2>&1; then
	ARCH="32"
	ARCHNAME="i386"
	JAVAROOT="${INSTDIR}/jre"
	DEFAULT_RAM="768m"

else
	echo "couldn't determine 32/64 bit of java"
	exit 1
fi

echo "${ARCH}-bit java detected"
JAVA="${JAVAROOT}/bin/java"
MAIN="zombie.javamods.Main"
CLASSPATH=".:./*:$("${JAVA}" -classpath javamods.jar ${MAIN} -bootstrap)"
LD_LIBRARY_PATH="${LD_LIBRARY_PATH}:./:./linux${ARCH}:${JAVAROOT}/lib/${ARCHNAME}"
LD_PRELOAD="${LD_PRELOAD}:./libjsig.so"
"${JAVA}" \
	-classpath "${CLASSPATH}" -Djava.awt.headless=true -XstartOnFirstThread \
	-Xms${DEFAULT_RAM} -Xmx${DEFAULT_RAM} \
	-Dzomboid.steam=0 -Dzomboid.znetlog=1 \
	-Djava.library.path=linux${ARCH}/:./ \
	-XX:-UseSplitVerifier -Djava.security.egd=file:/dev/urandom \
	${MAIN} -server "$@"

