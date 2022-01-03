NAME    := javamods

DIST    := dist
BUILD   := build
JARFILE := src/$(NAME).jar

ifeq ($(OS), Windows_NT)

EXT     := bat
OSNAME  := windows
ZOMBOID := TODO
ZSERVER := TODO

else

UNAME   := $(shell uname -s)

ifeq ($(UNAME), Linux)

EXT     := sh
OSNAME  := linux
ZOMBOID := $(HOME)/.steam/steam/steamapps/common/ProjectZomboid/projectzomboid
ZSERVER := $(HOME)/pzserver

else ifeq ($(UNAME), Darwin)

EXT     := sh
OSNAME  := macosx
ZOMBOID := TODO
ZSERVER := TODO

else

	$(error Unsupported platform $(UANME))

endif
endif

BUNDLE  := $(DIST)/$(NAME)-$(OSNAME).zip
SCRIPT  := scripts/$(NAME)-$(OSNAME).$(EXT)
SERVER  := scripts/$(NAME)-$(OSNAME)-server.$(EXT)
TARGET  := $(DIST)/javamods

all: bundle

bundle: build
	mkdir -p $(DIST)
	cd $(BUILD)/$(OSNAME) && zip ../../$(BUNDLE)   *

build: buildjar
	mkdir -p      $(BUILD)/$(OSNAME)               && \
	cp $(JARFILE) $(BUILD)/$(OSNAME)               && \
	cp $(SCRIPT)  $(BUILD)/$(OSNAME)/$(NAME).sh    && \
	cp $(SERVER)  $(BUILD)/$(NAME)-server-start.sh

buildjar:
	$(MAKE) -C src build

clean:
	rm -rf $(BUILD) $(DIST)
	$(MAKE) -C src clean

installzomboid:
	[ -d "$(ZOMBOID)" ] && unzip -o -d "$(ZOMBOID)" "$(BUNDLE)"

installzserver:
	[ -d "$(ZSERVER)" ] && unzip -o -d "$(ZSERVER)" "$(BUNDLE)"

install: installzomboid installzserver

uninstallzomboid:
	[ -d "$(ZOMBOID)" ] && rm -f "$(ZOMBOID)/$(NAME).sh" "$(ZOMBOID)/$(NAME).jar"

uninstallzserver:
	[ -d "$(ZSERVER)" ] && rm -f "$(ZSERVER)/$(NAME).sh" "$(ZSERVER)/java/$(NAME).jar" "$(ZSERVER)/ProjectJomboid64"*

uninstall: clean uninstallzomboid uninstallzserver
