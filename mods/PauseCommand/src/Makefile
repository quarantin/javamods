NAME     := $(shell grep ^id ../mod.info | cut -f2 -d=)
ZOMBOID  := $(HOME)/pzserver/java
JAVAMODS := ../lib/javamods.jar
JAR      := $(NAME).jar

build:
	javac -cp "$(ZOMBOID):$(JAVAMODS)" CommandBase.java zombie/commands/serverCommands/*.java && \
	jar -cf "$(JAR)" javamods.txt zombie/commands/serverCommands/*.class

install: build
	cp CommandBase.class ~/pzserver/java/zombie/commands
	cp zombie/commands/serverCommands/*.class ~/pzserver/java/zombie/commands/serverCommands

clean:
	rm -f *.jar zombie/commands/serverCommands/*.class
