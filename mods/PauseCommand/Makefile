NAME      := $(shell grep ^id mod.info | cut -f2 -d=)
BUNDLE    := dist
BUNDLEDIR := $(BUNDLE)/$(NAME)
JAR       := src/$(NAME).jar

all: bundle

build:
	$(MAKE) -C src build

bundle: build
	mkdir -p $(BUNDLEDIR)                    && \
	cp -r mod.info media $(JAR) $(BUNDLEDIR) && \
	cd $(BUNDLE) && zip -r $(NAME).zip $(NAME)

bundlesteam: bundle

clean:
	rm -rf dist && \
	$(MAKE) -C src clean

.PHONY: build dist
