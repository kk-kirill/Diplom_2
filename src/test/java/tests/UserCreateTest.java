package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.UserModel;
import org.junit.Test;

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
}