JAVAMODS := JavaMods.jar
WARNINGS := -Xlint:deprecation -Xlint:unchecked
SCRIPT   := scripts/JavaMods.sh
ZOMBOID := $(HOME)/.steam/steam/steamapps/common/ProjectZomboid/projectzomboid/

build:
	javac -cp "$(ZOMBOID)" $(WARNINGS) zombie/javamods/*.java && \
	jar -cf "$(JAVAMODS)" zombie/

clean:
	rm -f *.jar zombie/javamods/*.class

install: build
	cp "$(JAVAMODS)" "$(SCRIPT)" "$(ZOMBOID)"

uninstall:
	rm -f "$(ZOMBOID)/$(JAVAMODS)" "$(ZOMBOID)/$(SCRIPT)"
