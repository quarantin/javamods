JAR       := javamods.jar
CONFIG    := config.json
WARNINGS  := -Xlint:all
SOURCES   := */*.java */*/*.java
CLASSES   := */*.class */*/*.class
JAVASSIST := ../lib/javassist.jar
ZOMBOID   := $(HOME)/.steam/steam/steamapps/common/ProjectZomboid/projectzomboid/

build: clean
	javac --release 15 $(WARNINGS) -cp $(ZOMBOID):$(JAVASSIST) $(SOURCES) && \
	jar cfm $(JAR) MANIFEST.MF $(CLASSES) $(CONFIG)

clean:
	rm -f "$(JAR)" $(CLASSES)
