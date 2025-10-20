# Makefile for Life manager docker-compose stack

SHELL := /bin/bash

PROJECT_NAME ?= life-manager
COMPOSE_FILE ?= docker-compose.yaml
DC := docker compose -p $(PROJECT_NAME) -f $(COMPOSE_FILE)

.DEFAULT_GOAL := help

.PHONY: help
help:
	@echo "Available commands:"
	@awk 'BEGIN {FS = ":.*##"} /^[a-zA-Z0-9_.%-]+:.*##/ { printf "  \033[36m%-16s\033[0m %s\n", $$1, $$2 }' $(MAKEFILE_LIST)

.PHONY: up
up:
	$(DC) up -d --build

.PHONY: down
down:
	$(DC) down

.PHONY: nuke
nuke:
	$(DC) down -v