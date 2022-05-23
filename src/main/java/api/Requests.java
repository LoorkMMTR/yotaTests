package api;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;
import static java.lang.System.getProperty;
import static org.awaitility.Awaitility.await;

public interface Requests {
    @Step("Вызов метода 'getToken'")
    default Response callGetToken(JSONObject body) {
        return given()
                .body(body.toString())
                .contentType(ContentType.JSON)
                .when()
                .post("/login")
                .then()
                .log().body()
                .extract().response();
    }

    @Step("Вызов метода 'getEmptyPhone'")
    default Response callGetEmptyPhone() {
        return given()
                .contentType(ContentType.JSON)
                .header("authToken", getProperty("authToken"))
                .when()
                .get("/simcards/getEmptyPhone")
                .then()
                .log().body()
                .extract().response();
    }

    @Step("Вызов метода 'postCustomer'")
    default Response callPostCustomer(String freeNumber) {
        JSONObject postCustomerBody = new JSONObject()
                .put("name", "testCustomerName")
                .put("phone", freeNumber)
                .put("additionalParameters", new JSONObject().put("string", "anyString"));

        return given().log().body()
                .body(postCustomerBody.toString())
                .contentType(ContentType.JSON)
                .header("authToken", getProperty("authToken"))
                .when()
                .post("/customer/postCustomer")
                .then()
                .log().body()
                .extract().response();
    }

    @Step("Вызов метода 'getCustomerById'")
    default Response callGetCustomerById(String customerId) {
        return given()
                .contentType(ContentType.JSON)
                .header("authToken", getProperty("authToken"))
                .when()
                .get("/customer/getCustomerById?customerId=" + customerId)
                .then()
                .log().body()
                .extract().response();
    }

    @Step("Вызов метода 'findByPhoneNumber'")
    default Response callFindByPhoneNumber(String phone) {
        String requestBody = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<ns3:Envelope xmlns:ns2=\"soap\" xmlns:ns3=\"http://schemas.xmlsoap.org/soap/envelope\">\n" +
                "    <ns2:Header>\n" +
                "        <authToken>" + getProperty("authToken") + "</authToken>\n" +
                "    </ns2:Header>\n" +
                "    <ns2:Body>\n" +
                "        <phoneNumber>" + phone + "</phoneNumber>\n" +
                "    </ns2:Body>\n" +
                "</ns3:Envelope>";

        return given()
                .body(requestBody)
                .contentType(ContentType.XML)
                .when()
                .post("/customer/findByPhoneNumber")
                .then()
                .log().all()
                .extract().response();
    }

    @Step("Вызов метода 'changeCustomerStatus'")
    default Response callChangeCustomerStatus(String customerId, String newStatus) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("status", newStatus);
        return given()
                .body(requestBody.toString())
                .contentType(ContentType.JSON)
                .header("authToken", getProperty("authToken"))
                .when()
                .post("/customer/" + customerId + "/changeCustomerStatus")
                .then()
                .log().all()
                .extract().response();
    }
}
