package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.UserModel;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserCreateTest extends BaseTest {

    @DisplayName("Создание уникального пользователя")
    @Description("Проверяем, что уникального пользователя можно создать")
    @Test
    public void testCreateUniqueUser() {
        UserModel user = new UserModel(
                "test" + System.currentTimeMillis() + "@yandex.ru",
                "password123",
                "Ivan"
        );

        Response response = userApi.register(user);

        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @DisplayName("Создание уже зарегистрированного пользователя")
    @Description("Проверяем, что повторно создать пользователя нельзя")
    @Test
    public void testCreateDuplicateUser() {
        UserModel user = new UserModel(
                "test" + System.currentTimeMillis() + "@yandex.ru",
                "password123",
                "Ivan"
        );

        // Первый раз — успешно
        userApi.register(user);

        // Второй раз — ошибка
        Response response = userApi.register(user);

        response.then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @DisplayName("Создание пользователя без email")
    @Description("Проверяем ошибку, если не заполнен email")
    @Test
    public void testCreateUserWithoutEmail() {
        UserModel user = new UserModel(
                null,
                "password123",
                "Ivan"
        );

        Response response = userApi.register(user);

        response.then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @DisplayName("Создание пользователя без пароля")
    @Description("Проверяем ошибку, если не заполнен пароль")
    @Test
    public void testCreateUserWithoutPassword() {
        UserModel user = new UserModel(
                "test" + System.currentTimeMillis() + "@yandex.ru",
                null,
                "Ivan"
        );

        Response response = userApi.register(user);

        response.then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @DisplayName("Создание пользователя без имени")
    @Description("Проверяем ошибку, если не заполнено имя")
    @Test
    public void testCreateUserWithoutName() {
        UserModel user = new UserModel(
                "test" + System.currentTimeMillis() + "@yandex.ru",
                "password123",
                null
        );

        Response response = userApi.register(user);

        response.then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}