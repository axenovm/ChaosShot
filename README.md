# chaosShot

Микросервисный проект для работы с акциями и заказами в рюмочной.

## Сервисы

### `promotion-service`
- Управляет акциями (создание, обновление, удаление, получение).
- Использует паттерн Outbox: при создании акции пишет событие в таблицу `outbox`.
- Фоновый publisher читает неотправленные события и отправляет их в Kafka.
- Есть планировщик автосоздания акций по cron.

### `order-service`
- Управляет заказами.
- Подписывается на Kafka-топик `promotions-events`.
- Принимает `PromotionEvent` и обновляет локальную таблицу `order_promotions`.
- Использует данные о промо для применения скидок в логике заказов.

## Технологический стек
- Java 23
- Spring Boot 3.3
- Spring Web
- Spring Data JDBC
- Spring Kafka
- PostgreSQL
- Liquibase
- Gradle
- Lombok
- OpenAPI/Swagger (springdoc)
- Docker

## Интеграция через события
- Producer: `promotion-service`
- Topic: `promotions-events`
- Consumer: `order-service`
- Формат события: `PromotionEvent` (id события, тип, временные поля, данные акции)

