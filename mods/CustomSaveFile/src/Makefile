NAME     := $(shell grep ^id ../mod.info | cut -f2 -d=)
ZOMBOID  := $(HOME)/.steam/steam/steamapps/common/ProjectZomboid/projectzomboid/
JAVAMODS := ../lib/javamods.jar
JAR      := $(NAME).jar

build:
	javac -cp "$(ZOMBOID):$(JAVAMODS)" javamods/mods/*.java && \
	jar -cf "$(JAR)" javamods.txt javamods/mods/*.class

clean:
	rm -f *.jar javamods/mods/*.class
