# jms-activemq-order-pipeline

> **Portfólio:** Este projeto faz parte do meu portfólio voltado a back-end Java, com foco em **mensageria (JMS/ActiveMQ)**, **processamento assíncrono** e **boas práticas de integração entre microsserviços**.

## 🧠 Visão Geral

Pipeline assíncrono e desacoplado com **três serviços Spring Boot 3** que trocam mensagens via **ActiveMQ (JMS)**:

1️⃣ **order-api** → expõe `POST /orders`, valida a carga e publica na fila **`orders.new`**.  
2️⃣ **billing-worker** → consome **`orders.new`**, simula faturamento (gera `invoiceNumber` e `billedAt`) e publica **`orders.billed`**.  
3️⃣ **notification-worker** → consome **`orders.billed`** e **loga** “notificando cliente do pedido X”.

---

## 🧩 Arquitetura

```mermaid
flowchart LR
  subgraph Producer
    A[order-api\nPOST /orders] -->|JMS publish| Q1[(orders.new)]
  end

  subgraph Worker 1
    Q1 --> B[billing-worker\n@JmsListener] -->|JMS publish| Q2[(orders.billed)]
  end

  subgraph Worker 2
    Q2 --> C[notification-worker\n@JmsListener]
  end
```

**Características principais**
- Comunicação **assíncrona** (cliente recebe `202 Accepted` imediatamente).
- Serviços **independentes** (sem chamadas HTTP diretas).
- Mensagens em **JSON**, convertidas via `MappingJackson2MessageConverter`.

---

## ⚙️ Stack

- ☕ **Java 17**
- 🚀 **Spring Boot 3.x**
- 💬 **ActiveMQ Classic** (`rmohr/activemq:5.15.9`) via **Docker Compose**
- 📦 **JMS** (`spring-boot-starter-activemq`)
- 🔧 **Maven Wrapper** (`mvnw` / `mvnw.cmd`)

---

## 📁 Estrutura de Pastas

```
jms-activemq-order-pipeline/
├─ docker-compose.yml
├─ README.md
├─ order-api/
│  └─ src/main/java/br/com/orderapi/order_api/...
├─ billing-worker/
│  └─ src/main/java/br/com/billingworker/billing_worker/...
└─ notification-worker/
   └─ src/main/java/br/com/notificationworker/notification_worker/...
```

---

## 🧱 Pré-requisitos

- **Docker** + **Docker Compose**
- **JDK 17**
- (Opcional) **Maven** instalado — os subprojetos incluem Maven Wrapper

---

## 🐋 Subindo o ActiveMQ

Na **raiz** do repositório:

```bash
docker-compose up -d
```

Acesse o painel do ActiveMQ:  
➡️ [http://localhost:8161](http://localhost:8161)  (usuário/senha: `admin` / `admin`)

Portas de interesse:
- **61616** → JMS (TCP)
- **8161** → Console Web

---

## 🔌 Configurações dos Serviços

Todos os serviços compartilham a mesma configuração base:

```yaml
spring:
  activemq:
    broker-url: tcp://localhost:61616
    user: admin
    password: admin
  jms:
    pub-sub-domain: false  # filas (queues)
```

Portas HTTP padrão:
- **order-api:** 8081  
- **billing-worker:** 8082  
- **notification-worker:** 8083  

---

## ▶️ Como Rodar (local)

Abra **três terminais** (um para cada serviço):

```bash
# Terminal 1
cd order-api
./mvnw spring-boot:run

# Terminal 2
cd billing-worker
./mvnw spring-boot:run

# Terminal 3
cd notification-worker
./mvnw spring-boot:run
```

---

## 🧪 Teste Rápido

Envie um pedido:

```bash
curl -X POST http://localhost:8081/orders   -H "Content-Type: application/json"   -d '{
    "orderId": "ORD-001",
    "customerEmail": "cliente@example.com",
    "item": "Combo X",
    "quantity": 2
  }'
```

### Fluxo esperado nos logs:

1️⃣ `order-api` → *Published new order to orders.new*  
2️⃣ `billing-worker` → *Billing received order... Published billed order to orders.billed*  
3️⃣ `notification-worker` → *Notifying customer about order id=ORD-001 (invoice=NF-..., email=cliente@example.com)*

---

## 📨 Contratos de Mensagens

### OrderPayload (`orders.new`)
```json
{
  "orderId": "ORD-001",
  "customerEmail": "cliente@example.com",
  "item": "Combo X",
  "quantity": 2
}
```

### BilledOrder (`orders.billed`)
```json
{
  "orderId": "ORD-001",
  "customerEmail": "cliente@example.com",
  "item": "Combo X",
  "quantity": 2,
  "invoiceNumber": "NF-<uuid>",
  "billedAt": "2025-10-11T19:22:33.123Z"
}
```

---

## 📚 Boas Práticas Demonstradas

- **Desacoplamento via JMS** — cada serviço só conhece suas filas.  
- **Retorno assíncrono** (HTTP 202 Accepted) — processamento continua em background.  
- **Conversão JSON ↔ JMS** padronizada (`MappingJackson2MessageConverter`).  
- **Separação de responsabilidades** clara entre API, faturamento e notificação.  
- **Observabilidade** rápida via console do ActiveMQ.

---

## 🛠️ Troubleshooting

- **Connection refused / Could not connect:**  
  Verifique se o container do ActiveMQ está rodando (`docker ps`).  

- **Mensagens não chegando:**  
  Confira no console do ActiveMQ se as filas `orders.new` e `orders.billed` estão sendo populadas.  

- **Falha de conversão:**  
  Garanta que o `_type` da mensagem exista nos `typeIdMappings` (ex.: `"BilledOrder"`).  

- **Portas em uso:**  
  Altere `server.port` no `application.yml` do serviço em conflito.  

---

## 🧪 Build & Test

```bash
# Em cada subprojeto
./mvnw clean verify
```

---

## 🗺️ Roadmap

- Logs estruturados (JSON) e correlação por `orderId`
- Métricas básicas e health checks
- Retry/DLQ para mensagens com falha
- Testes de integração com ActiveMQ em container

---

## ✅ Status

- ✅ **order-api** (REST + publish `orders.new`)  
- ✅ **billing-worker** (consume `orders.new` + publish `orders.billed`)  
- ✅ **notification-worker** (consume `orders.billed` + log)  

---

## 🧾 Sugestões de Commits

- `feat: add docker-compose with ActiveMQ broker`
- `feat(order-api): expose POST /orders and publish to orders.new`
- `feat(billing-worker): consume orders.new and publish orders.billed`
- `feat(notification-worker): consume orders.billed and log notification`
- `chore: configure MappingJackson2MessageConverter with typeId mappings`
- `docs: update README after notification-worker rename`

---

## 📜 Licença

Este projeto é público e educativo.  
Use livremente como base de estudo ou demonstração de arquitetura assíncrona com JMS.
