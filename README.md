# Сервис пользователей

Создание DB
```sh
docker compose up -d --wait
```

Команда подготавливает PostgreSQL - `localhost:55432`, база `users_service`, логин и пароль `advsaasdev`. Демо-пользователи и администратор создаются при первом запуске

## Проверка в Postman

`http://localhost:8080/api/v1`. Демо-вход - `reader1@example.test` / `demo-password`, администратор - `admin` / `admin`.

| Запрос | Что проверить |
| --- | --- |
| `POST /auth/register` | Регистрация |
| `POST /auth/login` | Вход и получение токена |
| `GET /users/{id}` | Публичный профиль |
| `GET /users?ids=1,2` | Несколько публичных профилей |
| `PATCH /users/me` | Изменение своего профиля |
| `DELETE /users/me` | Удаление своего аккаунта |
| `DELETE /users/{id}` | Удаление пользователя (администратор) |
| `GET /roles` | Список ролей |
| `POST /roles` | Создание роли (администратор) |
| `PUT /users/{id}/roles/{roleId}` | Назначение роли (администратор) |
| `DELETE /users/{id}/roles/{roleId}` | Снятие роли (администратор) |
