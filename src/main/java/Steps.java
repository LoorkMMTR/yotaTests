import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static java.lang.System.getProperty;
import static java.lang.System.getenv;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public interface Steps extends Requests {
    @Step("Установка дефолтных параметров")
    default void setBasePath() {
        RestAssured.baseURI = getProperty("defaultURL");
        RestAssured.port = Integer.parseInt(getProperty("defaultPort"));
    }

    @Step("Получение токена авторизации")
    default void getAuthToken() {
        JSONObject requestBody = new JSONObject().put("login", "").put("password", "");
        if (Objects.equals(getenv("user"), "user")) {
            requestBody
                    .put("login", getProperty("userLogin"))
                    .put("password", getProperty("userPassword"));
        } else {
            requestBody
                    .put("login", getProperty("adminLogin"))
                    .put("password", getProperty("adminPassword"));
        }//TODO make DataProvider with both users
        System.setProperty("authToken", callGetToken(requestBody).path("token"));
        System.out.println(System.getProperty("authToken"));//TODO delete
    }

    @Step("Получение списка свободных номеров")
    default List<Long> getEmptyPhonesList() {
            return await()
                    .atMost(10, TimeUnit.SECONDS)
                    .pollInterval(500, TimeUnit.MILLISECONDS)
                    .until(() -> callGetEmptyPhone()
                            .body()
                            .jsonPath()
                            .getList("phones.phone"), hasItem(notNullValue()));
    }

    @Step("Проверка списка свободных номеров")
    default void checkEmptyPhonesList(List<Long> phonesList) {
        phonesList.forEach(phone -> {
            String customerId = callFindByPhoneNumber(phone.toString()).xmlPath().getString("**.find {it.name == 'customerId'}");
            assertThat("FAIL: Номер телефона "+phone+" занят абонентом с id"+customerId, customerId, is(emptyOrNullString()));
            System.out.println("OK: Номер телефона "+phone+" свободен");//TODO delete
        });
    }

    @Step("Проверка customerId")
    default void checkFindByPhoneNumber(String phone) {
            assertThat("FAIL: Номер телефона "+ phone +" свободен",
                    callFindByPhoneNumber(phone).xmlPath().getString("**.find {it.name == 'customerId'}"),
                    notNullValue());
            System.out.println("OK: Номер телефона "+ phone +" занят");//TODO delete
    }

//    @Step("Выполнение метода 'postCustomer'")
//    default void postCustomer() {
//
//        String id = given()
//                .contentType(JSON)
//                .header("authToken", getProperty("authAdminToken"))
//                .body(customer)
//                .when()
//                .post("/customer/postCustomer")
//                .prettyPeek()
//                .then()
//                .statusCode(200)
//                .extract()
//                .path("id");
//
//        customer.setCustomerId(id);
//        System.out.println("customerId = " + customer.getCustomerId());//TODO delete
//    }
//
//    @Step("Проверка создания клиента")
//    default void checkPostCustomerCreation() {
//        assertThat(customer.getCustomerId(), matchesRegex("[0-9,a-z]{8}(-[0-9,a-z]{4})*-[0-9,a-z]{12}"));
//    }

}
