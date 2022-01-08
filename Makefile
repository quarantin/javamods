LIB          := lib
SRC          := src
DIST         := dist
BUILD        := build
JAVABUILD    := $(BUILD)/java
SCRIPTS      := scripts
JAVAMODS     := javamods
JAVAMODSJAR  := $(JAVAMODS).jar
JAVAMODSPATH := $(SRC)/$(JAVAMODSJAR)
JAVASSIST    := javassist
JAVASSISTJAR := $(JAVASSIST).jar
SCRIPTNAME   := $(JAVAMODS)-start-server.sh
SCRIPTPATH   := $(SCRIPTS)/$(SCRIPTNAME)
VERSION      := $(shell git tag -l | tail -n 1)
BUNDLE       := $(DIST)/$(JAVAMODS)-$(VERSION).zip
TARGET       := $(DIST)/$(JAVAMODS)
ZOMBOID      := $(HOME)/.steam/steam/steamapps/common/ProjectZomboid/projectzomboid
PZSERVER     := $(HOME)/pzserver

all: bundle

bundle: build
	mkdir -p $(DIST)
	cd $(BUILD) && zip -r ../$(BUNDLE) *

build: buildjar updatejar
	mkdir -p           $(JAVABUILD) && \
	cp $(JAVAMODSPATH) $(JAVABUILD) && \
	cp $(SCRIPTPATH)   $(BUILD)

buildjar:
	$(MAKE) -C src build

updatejar:
	cd $(LIB) && \
	unzip -o -qq $(JAVASSISTJAR) "$(JAVASSIST)/*" && \
	jar uf ../$(JAVAMODSPATH) */*.class */*/*.class */*/*/*.class

clean:
	rm -rf $(BUILD) $(DIST)
	$(MAKE) -C src clean

installzomboid:
	[ -d "$(ZOMBOID)" ] && unzip -o -d "$(ZOMBOID)" "$(BUNDLE)" && \
	rm -f "$(ZOMBOID)/$(SCRIPTNAME)"

installzserver:
	[ -d "$(PZSERVER)" ] && unzip -o -d "$(PZSERVER)" "$(BUNDLE)" && \
	mv "$(PZSERVER)/$(JAVAMODSJAR)" "$(PZSERVER)/java"

install: clean bundle installzomboid installzserver

uninstallzomboid:
	[ -d "$(ZOMBOID)" ] && rm -f "$(ZOMBOID)/$(SCRIPTNAME)" "$(ZOMBOID)/$(JAVAMODSJAR)"

uninstallzserver:
	[ -d "$(PZSERVER)" ] && rm -f "$(PZSERVER)/$(SCRIPTNAME)" "$(PZSERVER)/java/$(JAVAMODSJAR)" "$(PZSERVER)/ProjectJomboid64"*

uninstall: clean uninstallzomboid uninstallzserver
