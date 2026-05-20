package com.hasl.gorest.api;

import com.hasl.gorest.client.ApiClient;
import com.hasl.gorest.factories.UserFactory;
import com.hasl.gorest.models.UserRequest;
import com.hasl.gorest.models.UserResponse;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserApiTest {

    private final ApiClient client = new ApiClient();  // 1 клиент на все тесты
    private int createdUserId;  // запоминаем ID для очистки
    private boolean isDeletedByTest = false;  // флаг: тест сам удалил пользователя

    // ==================================================
    // CREATE: проверить создание пользователя
    // ==================================================
    @Test
    public void createUserTest() {

        UserRequest request = UserFactory.validUser();          // берём данные из фабрики
        UserResponse response = client.createUser(request);     // отправляем запрос
        createdUserId = response.getId();                       // сохраняем ID

        assertThat(response.getId()).isPositive();              // ID > 0
        assertThat(response.getName()).isEqualTo(request.getName());
        assertThat(response.getEmail()).isEqualTo(request.getEmail());
        assertThat(response.getGender()).isEqualTo(request.getGender());
        assertThat(response.getStatus()).isEqualTo(request.getStatus());
    }

    // ==================================================
    // READ: проверить получение пользователя по ID
    // ==================================================
    @Test
    public void getUserTest() {

        UserRequest request = UserFactory.validUser();
        UserResponse created = client.createUser(request);      // сначала создаём
        createdUserId = created.getId();

        UserResponse fetched = client.getUser(created.getId()); // потом получаем

        // сравниваем, что данные совпадают
        assertThat(fetched.getId()).isEqualTo(created.getId());
        assertThat(fetched.getName()).isEqualTo(created.getName());
        assertThat(fetched.getEmail()).isEqualTo(created.getEmail());
        assertThat(fetched.getGender()).isEqualTo(created.getGender());
        assertThat(fetched.getStatus()).isEqualTo(created.getStatus());
    }

    // ==================================================
    // DELETE: проверить удаление пользователя
    // ==================================================
    @Test
    public void deleteUserTest() {

        UserRequest request = UserFactory.validUser();
        UserResponse created = client.createUser(request);
        createdUserId = created.getId();

        // до удаления — существует
        assertThat(client.existsUser(created.getId())).isTrue();

        client.deleteUser(created.getId());
        isDeletedByTest = true;  // помечаем, что пользователь уже удалён

        // после удаления — не существует
        assertThat(client.existsUser(created.getId())).isFalse();
    }

    // ==================================================
    // ОЧИСТКА: удаляем созданного пользователя после каждого теста
    // ==================================================
    @AfterMethod
    public void cleanUp() {
        // Удаляем только если:
        // 1. Есть ID пользователя
        // 2. Тест его ещё не удалил
        if (createdUserId != 0 && !isDeletedByTest) {
            client.deleteUser(createdUserId);
        }
        // Сбрасываем флаги для следующего теста
        createdUserId = 0;
        isDeletedByTest = false;
    }
}