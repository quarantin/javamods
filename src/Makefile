JAR       := javamods.jar
JARPATH   := ../$(JAR)
SCRIPT    := javamods.sh
ZOMBOID   := $(HOME)/.steam/steam/steamapps/common/ProjectZomboid/projectzomboid/
PZSERVER  := $(HOME)/.steam/steam/steamapps/common/Project Zomboid Dedicated Server/
WARNINGS  := -Xlint:deprecation -Xlint:unchecked
JAVASSIST := ../libs/javassist.jar

build:
	javac --release 15 -Xlint:all -cp "$(ZOMBOID):$(JAVASSIST)" $(WARNINGS) javamods/*.java javamods/mods/*.java && \
	jar -cfm "$(JARPATH)" MANIFEST.MF javamods/*.class javamods/mods/*.class

install: build
	cp $(JARPATH) $(ZOMBOID) && \
	cp ../scripts/javamods-linux.sh $(ZOMBOID)/$(SCRIPT)

installserver: build
	cp $(JARPATH) "$(PZSERVER)/java/" && \
	cp ../scripts/javamods-linux-server.sh "$(PZSERVER)/$(SCRIPT)"

run: install
	cd $(ZOMBOID) && ./$(SCRIPT)

debug: install
	cd $(ZOMBOID) && ./$(SCRIPT) -debug

bootstrap: install
	java -cp $(ZOMBOID)/javamods.jar javamods.Main -bootstrap

clean:
	rm -f "$(JARPATH)" javamods/*.class javamods/mods/*.class
