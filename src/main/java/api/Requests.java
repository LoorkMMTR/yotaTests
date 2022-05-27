package api;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;
import static java.lang.System.getProperty;

public interface Requests {
    private void addAttachment(@NotNull Response response) {
        Allure.addAttachment("Response", response.asString());
    }

    @Step("Вызов метода 'getToken'")
    default Response callGetToken(@NotNull JSONObject body) {
        Response response = given()
                .body(body.toString())
                .contentType(ContentType.JSON)
                .when()
                .post("/login")
                .then()
                .log().body()
                .extract().response();
        addAttachment(response);
        return response;
    }

    @Step("Вызов метода 'getEmptyPhone'")
    default Response callGetEmptyPhone(String token) {
        Response response = given()
                .contentType(ContentType.JSON)
                .header("authToken", token)
                .when()
                .get("/simcards/getEmptyPhone")
                .then()
                .log().body()
                .extract().response();
        addAttachment(response);
        return response;
    }

    @Step("Вызов метода 'postCustomer'")
    default Response callPostCustomer(String token, String freeNumber) {
        JSONObject postCustomerBody = new JSONObject()
                .put("name", "testCustomerName")
                .put("phone", freeNumber)
                .put("additionalParameters", new JSONObject().put("string", "anyString"));

        Response response = given().log().body()
                .body(postCustomerBody.toString())
                .contentType(ContentType.JSON)
                .header("authToken", token)
                .when()
                .post("/customer/postCustomer")
                .then()
                .log().body()
                .extract().response();
        addAttachment(response);
        return response;
    }

    @Step("Вызов метода 'getCustomerById'")
    default Response callGetCustomerById(String token, String customerId) {
        Response response = given()
                .contentType(ContentType.JSON)
                .header("authToken", token)
                .when()
                .get("/customer/getCustomerById?customerId=" + customerId)
                .then()
                .log().body()
                .extract().response();
        addAttachment(response);
        return response;
    }

    @Step("Вызов метода 'findByPhoneNumber'")
    default Response callFindByPhoneNumber(String token, String phone) {
        String requestBody = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<ns3:Envelope xmlns:ns2=\"soap\" xmlns:ns3=\"http://schemas.xmlsoap.org/soap/envelope\">\n" +
                "    <ns2:Header>\n" +
                "        <authToken>" + token + "</authToken>\n" +
                "    </ns2:Header>\n" +
                "    <ns2:Body>\n" +
                "        <phoneNumber>" + phone + "</phoneNumber>\n" +
                "    </ns2:Body>\n" +
                "</ns3:Envelope>";

        Response response = given()
                .body(requestBody)
                .contentType(ContentType.XML)
                .when()
                .post("/customer/findByPhoneNumber")
                .then()
                .log().all()
                .extract().response();
        addAttachment(response);
        return response;
    }

    @Step("Вызов метода 'changeCustomerStatus'")
    default Response callChangeCustomerStatus(String token, String customerId, String newStatus) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("status", newStatus);
        Response response = given()
                .body(requestBody.toString())
                .contentType(ContentType.JSON)
                .header("authToken", token)
                .when()
                .post("/customer/" + customerId + "/changeCustomerStatus")
                .then()
                .log().all()
                .extract().response();
        addAttachment(response);
        return response;
    }
}
