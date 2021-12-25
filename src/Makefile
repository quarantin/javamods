JAR := javamods.jar
SCRIPT := javamods.sh
ZOMBOID  := $(HOME)/.steam/steam/steamapps/common/ProjectZomboid/projectzomboid/
ZOMBOIDSERVER  := $(HOME)/.steam/steam/steamapps/common/Project Zomboid Dedicated Server/
WARNINGS := -Xlint:deprecation -Xlint:unchecked

build:
	javac --release 8 -cp "$(ZOMBOID)" $(WARNINGS) zombie/javamods/*.java zombie/javamods/mod/*.java && \
	jar -cf "$(JAR)" zombie/javamods/*.class zombie/javamods/mod/*.class

install: build
	cp $(JAR) $(ZOMBOID) && \
	cp ../scripts/javamods-linux.sh $(ZOMBOID)/$(SCRIPT)

installserver: build
	cp $(JAR) "$(ZOMBOIDSERVER)/java/" && \
	cp ../scripts/javamods-linux-server.sh "$(ZOMBOIDSERVER)/$(SCRIPT)"

run: install
	cd $(ZOMBOID) && ./$(SCRIPT)

debug: install
	cd $(ZOMBOID) && ./$(SCRIPT) -debug

bootstrap: install
	java -cp $(ZOMBOID)/javamods.jar zombie.javamods.Main -bootstrap

clean:
	rm -f *.jar zombie/javamods/*.class
