SHELL := /bin/bash
THIS_FILE := $(lastword $(MAKEFILE_LIST))
.PHONY : all

VERSION ?= dev
PUSH ?= 0
GPU ?= 0
H2O_BRANCH ?= $(shell git rev-parse --abbrev-ref HEAD)
GRADLE_VERSION ?= $(shell cat ../gradle/wrapper/gradle-wrapper.properties | grep distributionUrl | egrep -o '([0-9]+\.+)+[0-9]+')

ifeq ($(GPU),1)
	GPU_SUFFIX := -gpu
	BASE_FROM_IMAGE := nvidia/cuda:9.0-cudnn7-devel-ubuntu16.04
else
	BASE_FROM_IMAGE := ubuntu:16.04
endif

dev-base:
	docker build -t docker.h2o.ai/opsh2oai/h2o-3/dev-base$(GPU_SUFFIX):$(VERSION) \
		-f jenkins-images/Dockerfile-base \
		--build-arg FROM_IMAGE=$(BASE_FROM_IMAGE) \
		.
ifeq ($(PUSH),1)
	docker push docker.h2o.ai/opsh2oai/h2o-3/dev-base$(GPU_SUFFIX):$(VERSION)
endif

dev-jdk-8-base: dev-base
	docker build -t docker.h2o.ai/opsh2oai/h2o-3/dev-jdk-8-base$(GPU_SUFFIX):$(VERSION) \
		-f jenkins-images/Dockerfile-jdk-8-base \
		--build-arg GPU_SUFFIX=$(GPU_SUFFIX) \
		--build-arg FROM_VERSION=$(VERSION) \
		.
ifeq ($(PUSH),1)
	docker push docker.h2o.ai/opsh2oai/h2o-3/dev-jdk-8-base$(GPU_SUFFIX):$(VERSION)
endif

dev-jdk-others-base: dev-base
	docker build -t docker.h2o.ai/opsh2oai/h2o-3/dev-jdk-others-base$(GPU_SUFFIX):$(VERSION) \
		-f jenkins-images/Dockerfile-jdk-others-base \
		--build-arg GPU_SUFFIX=$(GPU_SUFFIX) \
		--build-arg FROM_VERSION=$(VERSION) \
		--build-arg H2O_BRANCH=$(H2O_BRANCH) \
		.
ifeq ($(PUSH),1)
	docker push docker.h2o.ai/opsh2oai/h2o-3/dev-jdk-others-base$(GPU_SUFFIX):$(VERSION)
endif

dev-python-base: dev-jdk-8-base
	docker build -t docker.h2o.ai/opsh2oai/h2o-3/dev-python-base$(GPU_SUFFIX):$(VERSION) \
		-f jenkins-images/Dockerfile-python-base \
		--build-arg GPU_SUFFIX=$(GPU_SUFFIX) \
		--build-arg FROM_VERSION=$(VERSION) \
		.
ifeq ($(PUSH),1)
	docker push docker.h2o.ai/opsh2oai/h2o-3/dev-python-base$(GPU_SUFFIX):$(VERSION)
endif

dev-r-base: dev-jdk-8-base
	docker build -t docker.h2o.ai/opsh2oai/h2o-3/dev-r-base$(GPU_SUFFIX):$(VERSION) \
		-f jenkins-images/Dockerfile-r-base \
		--build-arg GPU_SUFFIX=$(GPU_SUFFIX) \
		--build-arg FROM_VERSION=$(VERSION) \
		.
ifeq ($(PUSH),1)
	docker push docker.h2o.ai/opsh2oai/h2o-3/dev-r-base$(GPU_SUFFIX):$(VERSION)
endif

dev-python-%: dev-python-base
	docker build -t docker.h2o.ai/opsh2oai/h2o-3/dev-python-$*$(GPU_SUFFIX):$(VERSION) \
		-f jenkins-images/Dockerfile-python \
		--build-arg GPU_SUFFIX=$(GPU_SUFFIX) \
		--build-arg FROM_VERSION=$(VERSION) \
		--build-arg PYTHON_VERSION=$* \
		--build-arg H2O_BRANCH=$(H2O_BRANCH) \
		.
ifeq ($(PUSH),1)
	docker push docker.h2o.ai/opsh2oai/h2o-3/dev-python-$*$(GPU_SUFFIX):$(VERSION)
endif

dev-r-%: dev-r-base
	docker build -t docker.h2o.ai/opsh2oai/h2o-3/dev-r-$*$(GPU_SUFFIX):$(VERSION) \
		-f jenkins-images/Dockerfile-r \
		--build-arg GPU_SUFFIX=$(GPU_SUFFIX) \
		--build-arg FROM_VERSION=$(VERSION) \
		--build-arg R_VERSION=$* \
		.
ifeq ($(PUSH),1)
	docker push docker.h2o.ai/opsh2oai/h2o-3/dev-r-$*$(GPU_SUFFIX):$(VERSION)
endif

dev-jdk-%: dev-jdk-others-base
	docker build -t docker.h2o.ai/opsh2oai/h2o-3/dev-jdk-$*$(GPU_SUFFIX):$(VERSION) \
		-f jenkins-images/Dockerfile-jdk-others \
		--build-arg GPU_SUFFIX=$(GPU_SUFFIX) \
		--build-arg FROM_VERSION=$(VERSION) \
		--build-arg JAVA_VERSION=$* \
		.
ifeq ($(PUSH),1)
	docker push docker.h2o.ai/opsh2oai/h2o-3/dev-jdk-$*$(GPU_SUFFIX):$(VERSION)
endif

dev-build-base: dev-r-3.4.1
	docker build -t docker.h2o.ai/opsh2oai/h2o-3/dev-build-base$(GPU_SUFFIX):$(VERSION) \
		-f jenkins-images/Dockerfile-build-base \
		--build-arg GPU_SUFFIX=$(GPU_SUFFIX) \
		--build-arg FROM_VERSION=$(VERSION) \
		--build-arg H2O_BRANCH=$(H2O_BRANCH) \
		.
ifeq ($(PUSH),1)
	docker push docker.h2o.ai/opsh2oai/h2o-3/dev-build-base$(GPU_SUFFIX):$(VERSION)
endif

dev-build-gradle-%: dev-build-base
	docker build -t docker.h2o.ai/opsh2oai/h2o-3/dev-build-gradle-$*$(GPU_SUFFIX):$(VERSION) \
		-f jenkins-images/Dockerfile-build \
		--build-arg GPU_SUFFIX=$(GPU_SUFFIX) \
		--build-arg FROM_VERSION=$(VERSION) \
		--build-arg H2O_BRANCH=$(H2O_BRANCH) \
		.
ifeq ($(PUSH),1)
	docker push docker.h2o.ai/opsh2oai/h2o-3/dev-build-gradle-$*$(GPU_SUFFIX):$(VERSION)
endif

dev-build-hadoop-gradle-%: dev-build-gradle-$(GRADLE_VERSION)
	docker build -t docker.h2o.ai/opsh2oai/h2o-3/dev-build-hadoop-gradle-$*$(GPU_SUFFIX):$(VERSION) \
		-f jenkins-images/Dockerfile-build-hadoop \
		--build-arg GPU_SUFFIX=$(GPU_SUFFIX) \
		--build-arg FROM_VERSION=$(VERSION) \
		--build-arg H2O_BRANCH=$(H2O_BRANCH) \
		--build-arg GRADLE_VERSION=$(GRADLE_VERSION) \
		.
ifeq ($(PUSH),1)
	docker push docker.h2o.ai/opsh2oai/h2o-3/dev-build-hadoop-gradle-$*$(GPU_SUFFIX):$(VERSION)
endif

dev-release-gradle-%: dev-build-hadoop-gradle-$(GRADLE_VERSION)
	docker build -t docker.h2o.ai/opsh2oai/h2o-3/dev-release-gradle-$*$(GPU_SUFFIX):$(VERSION) \
		-f jenkins-images/Dockerfile-release \
		--build-arg GPU_SUFFIX=$(GPU_SUFFIX) \
		--build-arg FROM_VERSION=$(VERSION) \
		--build-arg H2O_BRANCH=$(H2O_BRANCH) \
		--build-arg GRADLE_VERSION=$(GRADLE_VERSION) \
		.
ifeq ($(PUSH),1)
	docker push docker.h2o.ai/opsh2oai/h2o-3/dev-release-gradle-$*$(GPU_SUFFIX):$(VERSION)
endif
