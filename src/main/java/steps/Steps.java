package steps;

import io.qameta.allure.Step;
import models.Customer;
import models.User;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class Steps {
    static User adminUser = new User("admin", "password");
    static User simpleUser = new User("user", "password");
    static Customer customer = new Customer("testCustomerName", "", new JSONObject().put("string","anyString"));

    @Step("Получение токена авторизации")
    public static void getAuthToken(boolean isAdminUser) {
        User resUser;
        if (isAdminUser) {
            resUser = adminUser;
        } else {
            resUser = simpleUser;
        }

        String authToken = given()
                .contentType(JSON)
                .accept(JSON)
                .body(resUser)
                .when()
                .post("/login")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .path("token");

        adminUser.setAuthToken(authToken);
        System.out.println("adminUserAuthToken = " + adminUser.getAuthToken());//TODO delete
    }

    @Step("Проверка токена авторизации")
    public static void checkAuthToken() {
        assertThat(adminUser.getAuthToken(), matchesRegex("[0-9,a-z]{32}"));
    }

    @Step("Получение свободного номера")
    public static void getEmptyPhoneNumber() {
        String phone = await()
                .ignoreExceptions()
                .atMost(20, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .until(() -> given()
                        .contentType(JSON)
                        .header("authToken", adminUser.getAuthToken())
                        .when()
                        .get("/simcards/getEmptyPhone")
                        .prettyPeek()
                        .then()
                        .extract()
                        .path("phones.phone[0]").toString(), is(notNullValue()));

        System.out.println("phoneNumber = " + phone);//TODO delete
        customer.setPhone(phone);
    }

    @Step("Проверка полученного номера телефона")
    public static void checkEmptyPhoneNumber() {
        assertThat(customer.getPhone(), matchesRegex("7\\d{10}"));
    }

    @Step("Выполнение метода 'postCustomer'")
    public static void postCustomer() {

        String id = given()
                .contentType(JSON)
                .header("authToken", adminUser.getAuthToken())
                .body(customer)
                .when()
                .post("/customer/postCustomer")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        customer.setCustomerId(id);
        System.out.println("customerId = " + customer.getCustomerId());//TODO delete
    }

    @Step("Проверка создания клиента")
    public static void checkPostCustomerCreation() {
        assertThat(customer.getCustomerId(), matchesRegex("[0-9,a-z]{8}(-[0-9,a-z]{4})*-[0-9,a-z]{12}"));
    }

}
