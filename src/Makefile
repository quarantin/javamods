JAVAMODS := javamods.jar
WARNINGS := -Xlint:deprecation -Xlint:unchecked
ZOMBOID  := $(HOME)/.steam/steam/steamapps/common/ProjectZomboid/projectzomboid/

build:
	javac -cp "$(ZOMBOID)" $(WARNINGS) zombie/*/*.java && \
	jar -cf "$(JAVAMODS)" zombie/

clean:
	rm -f *.jar zombie/javamods/*.class
