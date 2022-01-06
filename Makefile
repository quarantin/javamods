NAME     := javamods

DIST     := dist
BUILD    := build
JARNAME  := $(NAME).jar
JARFILE  := ./$(JARNAME)
AGENTJAR := javassist.jar
AGENT    := ./libs/$(AGENTJAR)

ifeq ($(OS), Windows_NT)

EXT      := bat
OSNAME   := windows
ZOMBOID  := TODO
ZSERVER  := TODO

else

EXT      := sh
OSNAME   := linux
ZOMBOID  := $(HOME)/.steam/steam/steamapps/common/ProjectZomboid/projectzomboid
ZSERVER  := $(HOME)/pzserver

endif

LAUNCHER := $(NAME)-$(OSNAME).$(EXT)
BUNDLE   := $(DIST)/$(NAME)-$(OSNAME).zip
SCRIPT   := scripts/$(LAUNCHER)
SERVER   := scripts/$(NAME)-$(OSNAME)-server.$(EXT)
TARGET   := $(DIST)/javamods

all: bundle

bundle: build
	mkdir -p $(DIST)
	cd $(BUILD)/$(OSNAME) && zip ../../$(BUNDLE) *

build: buildjar
	mkdir -p      $(BUILD)/$(OSNAME) && \
	cp $(AGENT)   $(BUILD)/$(OSNAME) && \
	cp $(JARFILE) $(BUILD)/$(OSNAME) && \
	cp $(SCRIPT)  $(BUILD)/$(OSNAME)/$(NAME).$(EXT) && \
	cp $(SERVER)  $(BUILD)/$(OSNAME)/$(NAME)-start-server.$(EXT)

buildjar:
	$(MAKE) -C src build

clean:
	rm -rf $(BUILD) $(DIST)
	$(MAKE) -C src clean

installzomboid:
	[ -d "$(ZOMBOID)" ] && unzip -o -d "$(ZOMBOID)" "$(BUNDLE)" && \
	rm -f "$(ZOMBOID)/$(SERVER)"

installzserver:
	[ -d "$(ZSERVER)" ] && unzip -o -d "$(ZSERVER)" "$(BUNDLE)"        && \
	mv "$(ZSERVER)/$(JARNAME)" "$(ZSERVER)/$(AGENTJAR)" "$(ZSERVER)/java" && \
	rm -f "$(ZSERVER)/$(NAME).$(EXT)"

install: clean bundle installzomboid installzserver

uninstallzomboid:
	[ -d "$(ZOMBOID)" ] && rm -f "$(ZOMBOID)/$(NAME).$(EXT)" "$(ZOMBOID)/$(JARNAME)"

uninstallzserver:
	[ -d "$(ZSERVER)" ] && rm -f "$(ZSERVER)/$(NAME)-start-server.$(EXT)" "$(ZSERVER)/java/$(JARNAME)" "$(ZSERVER)/ProjectJomboid64"*

uninstall: clean uninstallzomboid uninstallzserver
