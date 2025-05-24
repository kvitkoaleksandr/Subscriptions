# 📦 Subscriptions Service

## 📖 Описание проекта

`Subscriptions` — это микросервисное Java-приложение для управления пользователями и их подписками.  
Проект демонстрирует современные подходы к backend-разработке с использованием Spring Boot 3, кэширования в Redis, событийной интеграции с Kafka и документирования API через Swagger UI.

Сервис предоставляет REST API для:
- создания, получения, обновления и удаления пользователей;
- управления подписками пользователя;
- получения топ-3 самых популярных подписок среди всех пользователей.

Проект подходит как для демонстрации навыков, так и для расширения в рамках крупной системы.

---

## ⚙️ Стек технологий

- **Язык**: Java 17
- **Фреймворк**: Spring Boot 3.2.5
- **СУБД**: PostgreSQL 16
- **Кэширование**: Redis 7.2
- **Брокер сообщений**: Apache Kafka 7.6.0 + Zookeeper
- **Документирование API**: Swagger (springdoc-openapi 2.5.0)
- **Сборка**: Gradle (Kotlin DSL)
- **Контейнеризация**: Docker + Docker Compose
- **Логирование**: SLF4J + Lombok @Slf4j
- **Валидация**: Jakarta Validation (JSR 380)
- **Тестирование**:
  - JUnit 5
  - Mockito
  - Testcontainers (для интеграционных тестов)

---

## 🚀 Установка и запуск

### ✅ Предварительные требования

- Docker + Docker Compose
- JDK 17+
- Gradle (если запускать без Docker)
- IntelliJ IDEA / любой другой IDE

### 🔧 Локальный запуск (через Docker Compose)

1. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/yourusername/subscriptions.git
   cd subscriptions
   ```

2. Запустите сервисы:
   ```bash
   docker-compose up --build
   ```

3. Приложение будет доступно по адресу:
   - `http://localhost:8080` — приложение (если порт 8081 не переопределён)
   - `http://localhost:8080/swagger-ui.html` — Swagger UI

> ⚠️ Если приложение внутри контейнера слушает `8081`, убедитесь, что порт проброшен в `docker-compose.yml`.

---

### 🧪 Локальный запуск без Docker (через IDE)

1. Убедитесь, что:
   - PostgreSQL доступен на `localhost:5432`
   - Redis доступен на `localhost:6379`
   - Kafka и Zookeeper работают (можно через Docker)

2. Запустите `SubscriptionsApplication.java` из IDE.

---

## 📚 Структура проекта

```
.
├── controller/       → REST контроллеры
├── dto/              → DTO-классы
├── entity/           → JPA-сущности
├── exception/        → Глобальный обработчик ошибок
├── kafka/            → Kafka продюсер
├── mapper/           → Мапперы между DTO и Entity
├── repository/       → JPA-репозитории
├── service/          → Интерфейсы бизнес-логики
├── service/impl/     → Реализации сервисов
├── configuration/    → Конфигурации Redis и Redis HealthCheck
└── SubscriptionsApplication.java
```

---

## 📌 Особенности

- Использование `@Cacheable` с Redis для кэширования пользователей.
- Kafka-продюсер для события `users.created`.
- Гибкая архитектура, отделяющая контроллеры, сервисы и мапперы.
- Swagger-документация с удобным UI.
- Глобальный `@ControllerAdvice` для обработки ошибок.
- Поддержка валидации данных через `@Valid`.
