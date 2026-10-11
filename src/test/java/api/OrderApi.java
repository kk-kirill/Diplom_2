package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.OrderModel;

public class OrderApi {

    private final RequestSpecification requestSpec;

    public OrderApi(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    @Step("Создание заказа без авторизации")
    public Response create(OrderModel order) {
        return requestSpec
                .body(order)
                .when()
                .post("/api/orders");
    }

    @Step("Создание заказа с авторизацией")
    public Response createWithAuth(OrderModel order, String accessToken) {
        return requestSpec
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post("/api/orders");

    }
}
