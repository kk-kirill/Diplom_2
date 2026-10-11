package base;

import api.OrderApi;
import api.UserApi;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BaseTest {

    protected RequestSpecification requestSpec;
    protected UserApi userApi;
    protected OrderApi orderApi;

    public BaseTest() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru";
        requestSpec = given().header("Content-Type", "application/json");
        userApi = new UserApi(requestSpec);
        orderApi = new OrderApi(requestSpec);
    }
}
