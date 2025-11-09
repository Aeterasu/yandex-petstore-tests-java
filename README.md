# Petstore API Autotests

Тестовое задание на стажировку в Young&Yandex.

Пример проекта автоматизированных тестов для сервиса [Swagger Petstore](https://petstore.swagger.io).

Тесты покрывают основные HTTP-методы API: `GET`, `POST`, `PUT`, `DELETE`.

---

## Стэк

- Java 25
- JUnit 5
- Maven  
- Awaitility 

---

## Установка и запуск проекта локально

1. Клонировать репозиторий:

2. Убедиться, что установлен Maven и Java 25:

```
mvn -v
```

3. Собрать проект:

```
mvn clean install
```

4. Запустить тесты:

```
mvn test
```