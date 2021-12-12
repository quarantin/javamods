JAR := javamods.jar
SCRIPT := javamods.sh
ZOMBOID  := $(HOME)/.steam/steam/steamapps/common/ProjectZomboid/projectzomboid/
WARNINGS := -Xlint:deprecation -Xlint:unchecked

build:
	javac -cp "$(ZOMBOID)" $(WARNINGS) zombie/javamods/*.java zombie/javamods/mod/*.java && \
	jar -cf "$(JAR)" zombie/

install: build
	cp $(JAR) $(ZOMBOID) && \
	cp ../scripts/javamods-linux.sh $(ZOMBOID)/$(SCRIPT)

run: install
	cd $(ZOMBOID) && ./$(SCRIPT)

debug: install
	cd $(ZOMBOID) && ./$(SCRIPT) -debug

bootstrap: install
	java -cp $(ZOMBOID)/javamods.jar zombie.javamods.Main -bootstrap

clean:
	rm -f *.jar zombie/javamods/*.class
