package api;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.UserModel;

public class UserApi {

    private final RequestSpecification requestSpec;

    public UserApi(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    @Step("Создание пользователя")
    public Response register(UserModel user) {
        return requestSpec
                .body(user)
                .when()
                .post("/api/auth/register");
    }

    @Step("Логин пользователя")
    public Response login(UserModel user) {
        return requestSpec
                .body(user)
                .when()
                .post("/api/auth/login");
    }

    @Step("Удаление пользователя")
    public Response delete(String accessToken) {
        return requestSpec
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user");
    }
}
