package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.OrderModel;
import model.UserModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderCreateTest extends BaseTest {

    private UserModel user;
    private String accessToken;

    @Before
    public void setUp() {
        user = new UserModel(
                "test" + System.currentTimeMillis() + "@yandex.ru",
                "password123",
                "Ivan"
        );

        Response response = userApi.register(user);
        accessToken = response.then().extract().path("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userApi.delete(accessToken);
        }
    }

    @DisplayName("Создание заказа с авторизацией")
    @Description("Проверяем, что авторизованный пользователь может создать заказ")
    @Test
    public void testCreateOrderWithAuth() {
        OrderModel order = new OrderModel(List.of(
                "6acef97852135b001b21a32b",
                "6acef97852135b001b21a32e"
        ));

        Response response = orderApi.createWithAuth(order, accessToken);

        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @DisplayName("Создание заказа без авторизации")
    @Description("Проверяем, что неавторизованный пользователь может создать заказ")
    @Test
    public void testCreateOrderWithoutAuth() {
        OrderModel order = new OrderModel(List.of(
                "6acef97852135b001b21a32b",
                "6acef97852135b001b21a32e"
        ));

        Response response = orderApi.create(order);

        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверяем ошибку при пустом списке ингредиентов")
    @Test
    public void testCreateOrderWithoutIngredients() {
        OrderModel order = new OrderModel(List.of());

        Response response = orderApi.create(order);

        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @DisplayName("Создание заказа с неверным хешем ингредиента")
    @Description("Проверяем ошибку при невалидном хеше")
    @Test
    public void testCreateOrderWithWrongHash() {
        OrderModel order = new OrderModel(List.of("wrongHash123"));

        Response response = orderApi.create(order);

        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("One or more ids provided are incorrect"));
    }
}