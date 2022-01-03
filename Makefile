NAME    := javamods

DIST    := dist
BUILD   := build
JARNAME := $(NAME).jar
JARFILE := src/$(JARNAME)

ifeq ($(OS), Windows_NT)

EXT     := bat
OSNAME  := windows
ZOMBOID := TODO
ZSERVER := TODO

else

EXT     := sh
OSNAME  := linux
ZOMBOID := $(HOME)/.steam/steam/steamapps/common/ProjectZomboid/projectzomboid
ZSERVER := $(HOME)/pzserver

endif

SCRIPTNAME := $(NAME)-$(OSNAME).$(EXT)

BUNDLE  := $(DIST)/$(NAME)-$(OSNAME).zip
SCRIPT  := scripts/$(SCRIPTNAME)
SERVER  := scripts/$(NAME)-$(OSNAME)-server.$(EXT)
TARGET  := $(DIST)/javamods

all: bundle

bundle: build
	mkdir -p $(DIST)
	cd $(BUILD)/$(OSNAME) && zip ../../$(BUNDLE)   *

build: buildjar
	mkdir -p      $(BUILD)/$(OSNAME)                && \
	cp $(JARFILE) $(BUILD)/$(OSNAME)                && \
	cp $(SCRIPT)  $(BUILD)/$(OSNAME)/$(NAME).$(EXT) && \
	cp $(SERVER)  $(BUILD)/$(OSNAME)/$(NAME)-server-start.$(EXT)

buildjar:
	$(MAKE) -C src build

clean:
	rm -rf $(BUILD) $(DIST)
	$(MAKE) -C src clean

installzomboid:
	[ -d "$(ZOMBOID)" ] && unzip -o -d "$(ZOMBOID)" "$(BUNDLE)" && \
	rm -f "$(ZOMBOID)/$(SERVER)"

installzserver:
	[ -d "$(ZSERVER)" ] && unzip -o -d "$(ZSERVER)" "$(BUNDLE)" && \
	mv "$(ZSERVER)/$(JARNAME)" "$(ZSERVER)/java"                && \
	rm -f "$(ZSERVER)/$(NAME).$(EXT)"

install: bundle installzomboid installzserver

uninstallzomboid:
	[ -d "$(ZOMBOID)" ] && rm -f "$(ZOMBOID)/$(NAME).sh" "$(ZOMBOID)/$(JARNAME)"

uninstallzserver:
	[ -d "$(ZSERVER)" ] && rm -f "$(ZSERVER)/$(NAME)-server-start.sh" "$(ZSERVER)/java/$(JARNAME)" "$(ZSERVER)/ProjectJomboid64"*

uninstall: clean uninstallzomboid uninstallzserver
