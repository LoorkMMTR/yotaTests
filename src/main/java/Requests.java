import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.XML;

import javax.xml.crypto.dsig.XMLObject;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.XMLFormatter;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.http.ContentType.XML;
import static java.lang.System.getProperty;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.*;

public interface Requests {
    @Step("Вызов метода 'getToken'")
    default Response callGetToken(JSONObject body) {
        return given()
                .body(body.toString())
                .contentType(ContentType.JSON)
                .when()
                .post("/login")
                .then()
                .extract()
                .response();
    }

    @Step("Вызов метода 'getEmptyPhone'")
    default Response callGetEmptyPhone() {
        return given()
                .contentType(JSON)
                .header("authToken", getProperty("authToken"))
                .when()
                .get("/simcards/getEmptyPhone")
                .then()
                .extract()
                .response();
    }

    @Step("Вызов метода 'postCustomer'")
    default Response callPostCustomer(String freeNumber) {
        Customer customer = new Customer("testCustomerName", freeNumber, new JSONObject().put("string", "anyString"));
        return given()
                .body(customer)
                .contentType(JSON)
                .header("authToken", getProperty("authToken"))
                .when()
                .post("/customer/postCustomer")
                .then()
                .statusCode(200)
                .extract()
                .response().prettyPeek();
    }

    @Step("Вызов метода 'getCustomerById'")
    default Response callGetCustomerById(String customerId) {
        return given()
                .contentType(JSON)
                .header("authToken", getProperty("authToken"))
                .when()
                .get("/customer/getCustomerById?customerId=" + customerId)
                .then()
                .statusCode(200)
                .extract()
                .response();
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
                .contentType(XML)
                .when()
                .post("/customer/findByPhoneNumber")
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    @Step("Вызов метода 'changeCustomerStatus'")
    default void callChangeCustomerStatus(String customerId, String newStatus) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("status", newStatus);
        given()
                .body(requestBody)
                .contentType(JSON)
                .header("authToken", getProperty("authToken"))
                .when()
                .post("/customer/" + customerId + "/changeCustomerStatus")
                .prettyPeek()
                .then()
                .statusCode(200);
    }
}
