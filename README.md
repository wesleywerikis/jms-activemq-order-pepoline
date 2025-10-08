# jms-activemq-order-pipeline

> **Portfólio**: Este projeto faz parte do meu portfólio voltado a back-end Java, para demonstrar experiência com **mensageria (JMS/ActiveMQ)**, **processamento assíncrono** e **boas práticas de integração** entre serviços.

## Arquitetura

- **order-api** (Spring Boot Web + JMS): expõe `POST /orders`, valida a carga e publica a mensagem na fila **`orders.new`**.
- **billing-worker** (JMS Worker): consome **`orders.new`**, simula faturamento (gera NF e `billedAt`) e publica em **`orders.billed`**.
- **notification-worker** (JMS Worker): consome **`orders.billed`** e **loga** a mensagem "notificando cliente do pedido X".

Fila → Worker → Próxima Fila → Worker  
`orders.new` → **billing-worker** → `orders.billed` → **notification-worker**

## Stack

- Java 17
- Spring Boot 3.x
- **ActiveMQ Classic** (imagem `rmohr/activemq:5.15.9`) via Docker Compose
- JMS (spring-boot-starter-activemq)
- Maven Wrapper (mvnw)

## Pré-requisitos

- Docker + Docker Compose
- JDK 17
- (Opcional) Maven instalado — os subprojetos incluem Maven Wrapper

## Subindo o broker

Na pasta raiz:

```bash
docker-compose up -d
# Console: http://localhost:8161  (admin / admin)
