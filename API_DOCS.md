# GoRest API Documentation

## Base URL
`https://gorest.co.in/public/v2`

## Authentication
Bearer Token

## User Endpoints

| Step | Method | Endpoint | Headers | Body | Success | Error |
|------|--------|----------|---------|------|---------|-------|
| 1 | GET | /users | - | - | 200, array | - |
| 2 | POST | /users | `Authorization`, `Content-Type: JSON` | `{"name","email","gender","status"}` | 201, `id` | 422 |
| 3 | GET | /users/{id} | `Authorization` | - | 200, object | 404 |
| 4 | PUT | /users/{id} | `Authorization`, `Content-Type: JSON` | `{"name","email","gender","status"}` (all fields) | 200, updated | 404, 422 |
| 5 | DELETE | /users/{id} | `Authorization` | - | 204 (empty) | 404 |
| 6 | GET | /users/{id} (after delete) | `Authorization` | - | 404 | - |

## Example User JSON
```json
{
    "id": 1234567,
    "name": "Хасл Постман",
    "email": "hasl.postman.2026@mail.ru",
    "gender": "male",
    "status": "active"
}