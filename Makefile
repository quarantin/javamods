NAME          := javamods

BUILD         := build
BUILDLINUX    := $(BUILD)/linux
BUILDMACOSX   := $(BUILD)/macosx
BUILDWINDOWS  := $(BUILD)/windows

BUNDLE        := dist
BUNDLELINUX   := $(BUNDLE)/$(NAME)-linux.zip
BUNDLEMACOSX  := $(BUNDLE)/$(NAME)-macosx.zip
BUNDLEWINDOWS := $(BUNDLE)/$(NAME)-windows.zip

JARFILE       := src/$(NAME).jar
SCRIPTLINUX   := scripts/$(NAME)-linux.sh
SCRIPTMACOSX  := scripts/$(NAME)-macosx.sh
SCRIPTWINDOWS := scripts/$(NAME)-windows.bat
SCRIPTWINDOWSSERVER := scripts/$(NAME)-windows-server.bat

all: bundle

build: buildjar buildlinux buildmacosx buildwindows

buildjar:
	$(MAKE) -C src build

buildlinux:
	mkdir -p $(BUILDLINUX)      && \
	cp $(JARFILE) $(BUILDLINUX) && \
	cp $(SCRIPTLINUX) $(BUILDLINUX)/$(NAME).sh

buildmacosx:
	mkdir -p $(BUILDMACOSX)      && \
	cp $(JARFILE) $(BUILDMACOSX) && \
	cp $(SCRIPTMACOSX) $(BUILDMACOSX)/$(NAME).sh

buildwindows:
	mkdir -p                  $(BUILDWINDOWS) && \
	cp $(JARFILE)             $(BUILDWINDOWS) && \
	cp $(SCRIPTWINDOWS)       $(BUILDWINDOWS)/$(NAME).bat && \
	cp $(SCRIPTWINDOWSSERVER) $(BUILDWINDOWS)/$(NAME)-server.bat

bundle: build dist bundlelinux bundlemacosx bundlewindows

bundlelinux:
	cd $(BUILDLINUX) && zip ../../$(BUNDLELINUX) *

bundlemacosx:
	cd $(BUILDMACOSX) && zip ../../$(BUNDLEMACOSX) *

bundlewindows:
	cd $(BUILDWINDOWS) && zip ../../$(BUNDLEWINDOWS) *

dist:
	mkdir -p dist

clean: cleansrc
	rm -rf $(BUILD) $(BUNDLE)

cleansrc:
	$(MAKE) -C src clean

install: bundle
	./scripts/install-linux.sh

uninstall:
	./scripts/uninstall-linux.sh

.PHONY: build dist
