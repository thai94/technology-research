##@ Document
PROJECT_ROOT = $(shell pwd)
PROJECT_NAME ?= be-api
DOCKER_COMPOSE ?= docker-compose -p $(PROJECT_NAME)
GO ?= go

build: ## Build local environment
	docker build -t $(PROJECT_NAME) .

up: ## Run local environment
	@make build
	@$(DOCKER_COMPOSE) up -d

down: ## Shutdown local environment
	@$(DOCKER_COMPOSE) down