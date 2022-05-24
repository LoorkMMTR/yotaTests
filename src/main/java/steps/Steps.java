package steps;

import api.Requests;
import com.fasterxml.jackson.databind.*;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.Customer;
import org.awaitility.core.ConditionTimeoutException;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static io.qameta.allure.Allure.addAttachment;
import static java.lang.Integer.parseInt;
import static java.lang.System.*;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.AssertJUnit.fail;

public interface Steps extends Requests {

    @Step("Установка дефолтных параметров")
    default void setBasePath() {
        RestAssured.baseURI = getProperty("defaultURL");
        RestAssured.port = parseInt(getProperty("defaultPort"));
    }

    @Step("Получение токена авторизации")
    default String getAuthToken() {
        JSONObject requestBody = new JSONObject().put("login", "").put("password", "");
        if (Objects.equals(getProperty("user"), "user")) {
            requestBody
                    .put("login", getProperty("userLogin"))
                    .put("password", getProperty("userPassword"));
        } else if (Objects.equals(getProperty("user"), "admin")){
            requestBody
                    .put("login", getProperty("adminLogin"))
                    .put("password", getProperty("adminPassword"));
        } else {
            assertThat("Пользователь не указан или указан некорректно [user, admin]", Objects.equals(getProperty("user"), null));
        }//TODO make DataProvider with both users

        Response resp = callGetToken(requestBody);
        addAttachment("Response", resp.asString());
        return resp.path("token");
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
    default void checkEmptyPhonesListByPostCustomer(List<Long> phonesList) {
        phonesList.forEach(phone -> {
            Response resp = callPostCustomer(phone.toString());
            addAttachment("Response", resp.asString());
            String customerId = resp.path("id");
            assertThat("Номер телефона " + phone + " уже используется", customerId, is(not(emptyOrNullString())));
        });
    }

    @Step("Создание клиента методом 'postCustomer'")
    default List<String> callPostCustomerByPhonesList(List<Long> phonesList) {
        ArrayList<String> resList = new ArrayList<>();
        String customerId = null;
        int count = 0;
        while (count < phonesList.size()) {
            String singlePhone = phonesList.get(count).toString();
            customerId = callPostCustomer(singlePhone).path("id");
            if (customerId != null) {
                resList.add(singlePhone);
                resList.add(customerId);
                break;
            }
            count++;
        }
        assertThat("В списке нет ни одного свободного номера", customerId, is(notNullValue()));
        return resList;
    }

    @Step("Сохранение createdCustomerPhone и createdCustomerId")
    default void setCreatedUserPhoneAndId(List<String> resList) {
        System.setProperty("createdCustomerPhone", resList.get(0));
        System.setProperty("createdCustomerId", resList.get(1));
    }

    @Step("Проверка создания клиента")
    default void checkCustomerCreation(String customerId) {
        Response resp = callGetCustomerById(customerId);
        addAttachment("Response", resp.asString());
        assertThat(resp
                .then()
                .statusCode(200)
                .extract()
                .path("return.status"), is(not(emptyOrNullString())));
    }

    @Step("Проверка изменения статуса клиента")
    default void checkCustomerStatus(String customerId, String status, int durationSEC) {
        try {
            await()
                    .atMost(durationSEC, TimeUnit.SECONDS)
                    .pollInterval(1, TimeUnit.SECONDS)
                    .until(() -> callGetCustomerById(customerId)
                            .then()
                            .extract().response()
                            .path("return.status"), equalToIgnoringCase(status));//TODO addAttachment("Response", resp.asString())?
        } catch (ConditionTimeoutException e) {
            fail("По истечении " + durationSEC + " секунд статус отличается от " + status);
        }
    }

    @Step("Получение данных клиента по ИД")
    default Customer getCustomerDataById(String customerId) {
        Map<String, String> resMap = callGetCustomerById(customerId)
                .then()
                .extract()
                .jsonPath()
                .getJsonObject("return");
        ObjectMapper objectMapper = new ObjectMapper();
        Customer customer = objectMapper.convertValue(resMap, Customer.class);

        System.setProperty("customerPassportData", customer.getPd().replaceAll("\\D", " "));
        List<String> list = Arrays.asList(getProperty("customerPassportData").trim().split("\\s+"));
        customer.setPassportNumber(list.get(0));
        customer.setPassportSeries(list.get(1));
        return customer;
    }

    @Step("Проверка паспортных данных клиента")
    default void checkCustomerPassportData(Customer data) {
        assertThat("Некорректный номер паспорта" + data.getPassportNumber(), data.getPassportNumber(), is(matchesRegex("^\\d{6}$")));
        assertThat("Некорректная серия паспорта" + data.getPassportNumber(), data.getPassportSeries(), is(matchesRegex("^\\d{4}$")));
    }

    @Step("Проверка дополнительных данных клиента")
    default void checkCustomerAdditionalParameters(Customer data) {
        assertThat("Дополнительные параметры отсутствуют", data.additionalParameters.getString(), is(notNullValue()));
    }

    @Step("Получение ИД клиента из старой системы")
    default String getCustomerIdFromOldSystem(String customerPhone) {
        Response resp = callFindByPhoneNumber(customerPhone);
        addAttachment("Response", resp.asString());
        String customerId = resp.xmlPath().getString("Envelope.Body.customerId");
        assertThat("ИД в ответе метода не получен", customerId, is(not(emptyOrNullString())));
        return customerId;
    }

    @Step("Проверка ИД клиента из старой системы")
    default void checkCustomerIdFromOldSystem(String customerId, String oldSystemCustomerId) {
        assertThat("ИД " + oldSystemCustomerId + " , полученный из старой системы не соответствует" + customerId, oldSystemCustomerId, is(equalTo(customerId)));
    }

    @Step("Проверка ответа при изменении статуса клиента Пользователем")
    default void checkChangingStatusByUser(Response resp) {
        assertThat(resp.statusCode(), is(not(200)));
        assertThat(resp.then().extract().response(), is(notNullValue()));
        String errMsg = resp
                .then()
                .extract()
                .jsonPath()
                .getString("errorMessage");
        assertThat("Сообщение об ошибке не соответствует", errMsg,
                is("У пользователя не хватает прав на выполнение команды"));
    }
}
