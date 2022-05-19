package steps;

import api.Requests;
import com.fasterxml.jackson.databind.*;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import models.Customer;
import org.awaitility.core.ConditionTimeoutException;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.lang.Integer.parseInt;
import static java.lang.System.*;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.AssertJUnit.assertFalse;
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
        if (Objects.equals(getenv("user"), "user")) {
            requestBody
                    .put("login", getProperty("userLogin"))
                    .put("password", getProperty("userPassword"));
        } else {
            requestBody
                    .put("login", getProperty("adminLogin"))
                    .put("password", getProperty("adminPassword"));
        }//TODO make DataProvider with both users
        return callGetToken(requestBody).path("token");
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
            String customerId = callPostCustomer(phone.toString()).path("id");
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
    default void checkCustomerCreation(String customerId, String status) {
        assertThat(callGetCustomerById(customerId)
                .then()
                .statusCode(200)
                .extract()
                .path("return.status"), equalToIgnoringCase(status));
    }

    @Step("Проверка активации клиента")
    default void checkCustomerStatus(String customerId, String status, int durationSEC) {
        try {
            await()
                    .atMost(durationSEC, TimeUnit.SECONDS)
                    .pollInterval(500, TimeUnit.MILLISECONDS)
                    .until(() -> callGetCustomerById(customerId)
                            .then()
                            .extract()
                            .path("return.status"), equalToIgnoringCase(status));
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
        List<String> list = List.of(getProperty("customerPassportData").trim().split("\\s+"));
        customer.setPassportNumber(list.get(0));
        customer.setPassportSeries(list.get(1));
        return customer;
    }

    @Step("Проверка паспортных данных клиента")
    default void checkCustomerPassportData(Customer data) {
        assertThat("Некорректный номер паспорта"+data.getPassportNumber(), data.getPassportNumber(), is(matchesRegex("^\\d{6}$")));
        assertThat("Некорректная серия паспорта"+data.getPassportNumber(), data.getPassportSeries(), is(matchesRegex("^\\d{4}$")));
    }

    @Step("Проверка дополнительных данных клиента")
    default void checkCustomerAdditionalParameters(Customer data) {
        assertThat("Дополнительные параметры отсутствуют", data.additionalParameters.getString(), is(notNullValue()));
    }

    @Step("Получение ИД клиента по номеру телефона")
    default void getCustomerIdByPhone(String customerPhone) {
        callFindByPhoneNumber(customerPhone).then().statusCode(200);
    }
}
