# GoRest API Automation Framework

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://openjdk.org/projects/jdk/21/)
[![Maven](https://img.shields.io/badge/Maven-3.9.6-red.svg)](https://maven.apache.org/)
[![RestAssured](https://img.shields.io/badge/RestAssured-5.5.0-brightgreen.svg)](https://rest-assured.io/)
[![TestNG](https://img.shields.io/badge/TestNG-7.10.2-green.svg)](https://testng.org/)
[![Allure](https://img.shields.io/badge/Allure-2.29.0-orange.svg)](https://allurereport.org/)

## 📋 О проекте

Фреймворк для автоматизированного тестирования REST API [GoRest](https://gorest.co.in/).

**GoRest** — тестовое API для практики. Требует Bearer token авторизацию.  
Проект покрывает CRUD операции для сущности **User**, включая позитивные и негативные сценарии.

---

## 🚀 Стек технологий

| Технология | Версия | Назначение |
|------------|--------|------------|
| Java | 21 | язык программирования |
| Maven | 3.9.6 | сборка проекта, управление зависимостями |
| RestAssured | 5.5.0 | HTTP-клиент для отправки запросов |
| TestNG | 7.10.2 | запуск тестов, группы |
| AssertJ | 3.27.3 | читаемые проверки |
| Jackson | 2.18.2 | сериализация/десериализация JSON |
| Lombok | 1.18.36 | сокращение кода (геттеры, сеттеры, билдер) |
| Owner | 1.0.12 | управление конфигурацией |
| Allure | 2.29.0 | генерация отчётов |

---

## 📁 Структура проекта
gorest-api-tests/
├── pom.xml
├── README.md
├── .gitignore
│
├── src/main/java/com/hasl/gorest/
│ ├── client/
│ │ └── ApiClient.java # HTTP клиент (CRUD + exists)
│ ├── config/
│ │ ├── ApiConfig.java # Owner интерфейс
│ │ └── ConfigManager.java # синглтон для доступа к конфигу
│ ├── factories/
│ │ └── UserFactory.java # создание тестовых данных
│ └── models/
│ ├── UserRequest.java # DTO запроса (без id)
│ └── UserResponse.java # DTO ответа (с id)
│
├── src/main/resources/
│ └── config.properties # конфигурация (не в Git)
│
└── src/test/java/com/hasl/gorest/
├── api/
│ └── UserApiTest.java # тесты (7 штук)
├── config/
│ └── ConfigCheck.java # проверка конфига
└── simple_test/ # памятка (сырые тесты)
├── SimpleGetTest.java
├── SimplePostTest.java
├── SimplePutTest.java
├── SimplePatchTest.java
└── SimpleDeleteTest.java


---

## ⚙️ Настройка

### 1. Получить токен GoRest

1. Зарегистрируйся на [gorest.co.in](https://gorest.co.in/)
2. Перейди в **Account → Access Token**
3. Скопируй токен (выглядит как `d4e3da0dc5c5...`)

### 2. Создать `config.properties`

Скопируй пример и добавь свой токен:

```properties
base.url=https://gorest.co.in/public/v2
timeout=5000
api.token=твой_токен_сюда
Файл должен лежать в src/main/resources/config.properties

⚠️ Файл добавлен в .gitignore — токен не попадёт в GitHub

🧪 Запуск тестов
bash
# Запустить все тесты (regression)
mvn test -Dgroups=regression

# Только позитивные (create, get, update, delete)
mvn test -Dgroups=positive

# Только негативные (empty name, duplicate email, invalid id)
mvn test -Dgroups=negative

# Один конкретный тест
mvn test -Dtest=UserApiTest#createUserTest

# Все тесты без групп (включая simple_test)
mvn test
Allure отчёт
bash
# Сгенерировать и открыть отчёт
mvn allure:serve

📊 Покрытие тестов

Позитивные (4 теста)
Тест	Метод	Статус	Что проверяет
createUserTest	POST /users	201	создание пользователя, id > 0, поля совпадают
getUserTest	GET /users/{id}	200	получение по ID, данные совпадают
updateUserTest	PUT /users/{id}	200	полное обновление всех полей
deleteUserTest	DELETE /users/{id}	204	удаление, exists → false

Негативные (3 теста)
Тест	Метод	Ожидаемый статус	Что проверяет
createUserWithEmptyNameTest	POST /users	422	пустое имя → ошибка валидации
createUserWithDuplicateEmailTest	POST /users	422	дубликат email → 422
getUserWithInvalidIdTest	GET /users/999999999	404	несуществующий ID → 404

🧠 Архитектура (почему так)

Тест (UserApiTest)
    │
    ▼
ApiClient                     ← HTTP запросы, статус-коды, логи
    │
    ▼
UserRequest / UserResponse    ← DTO, типизация данных
    │
    ▼
UserFactory                   ← генерация данных, уникальный email
    │
    ▼
ConfigManager                 ← URL, токен из config.properties

Почему DTO?
→ компилятор проверяет имена полей, IDEA подсказывает, легко рефакторить

Почему Factory?
→ убирает дублирование, централизует генерацию уникального email

Почему ApiClient?
→ инкапсулирует HTTP, тесты не знают про URL/токен/RestAssured

Почему Owner?
→ конфиг без перекомпиляции, токен в безопасности

🔧 Технические детали
Уникальный email
java
private static String generateUniqueEmail() {
    return "test_" + System.currentTimeMillis() + "_" + UUID.randomUUID() + "@example.com";
}
System.currentTimeMillis() — уникальность в пределах секунды

UUID.randomUUID() — уникальность даже в одну миллисекунду

Очистка после тестов
java
@AfterMethod
public void cleanUp() {
    if (createdUserId != 0 && !isDeletedByTest) {
        client.deleteUser(createdUserId);
    }
}
isDeletedByTest — флаг, чтобы не удалять дважды (например, после deleteUserTest)

Существование пользователя
java
public boolean existsUser(int userId) {
    try {
        spec.get("/users/" + userId).then().statusCode(200);
        return true;
    } catch (AssertionError e) {
        return false;
    }
}
getUser() падает при 404, existsUser() ловит и возвращает false

📌 Группы тестов (TestNG)
Группа	Состав	Команда
positive	4 позитивных теста	mvn test -Dgroups=positive
negative	3 негативных теста	mvn test -Dgroups=negative
regression	все 7 тестов	mvn test -Dgroups=regression
example	simple_test (памятка)	mvn test -Dgroups=example

👤 Автор
Сергей Дубакин
GitHub: dubakinsergey

📄 Лицензия
Этот проект создан в учебных целях для изучения API-тестирования.
