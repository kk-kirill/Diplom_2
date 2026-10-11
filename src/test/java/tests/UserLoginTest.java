package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.UserModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class UserLoginTest extends BaseTest {

    private UserModel user;
    private String accessToken;

    @Before
    public void createUser() {
        user = new UserModel(
                "test" + System.currentTimeMillis() + "@yandex.ru",
                "password123",
                "Ivan"
        );

        Response response = userApi.register(user);
        accessToken = response.then().extract().path("accessToken");
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            userApi.delete(accessToken);
        }
    }

    @DisplayName("Логин существующего пользователя")
    @Description("Проверяем, что пользователь может войти в систему")
    @Test
    public void testLoginExistingUser() {
        Response response = userApi.login(user);

        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());
    }

    @DisplayName("Логин с неверными данными")
    @Description("Проверяем ошибку при неверном логине/пароле")
    @Test
    public void testLoginWithWrongCredentials() {
        UserModel wrongUser = new UserModel(
                user.getEmail(),
                "wrongPassword",
                user.getName()
        );

        Response response = userApi.login(wrongUser);

        response.then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}