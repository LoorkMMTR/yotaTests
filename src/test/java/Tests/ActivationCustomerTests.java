package Tests;

import io.qameta.allure.Feature;
import io.restassured.response.Response;
import models.Customer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import services.BaseTest;

import java.util.List;

import static java.lang.System.getProperty;

@Feature("Тестовое задание для Yota")
public class ActivationCustomerTests extends BaseTest {
    List<Long> phonesList;
    List<String> createdUserData;

    @BeforeMethod(description = "Создание клиента", onlyForGroups = "Общее предусловие")
    public void createCustomer() {
        phonesList = getEmptyPhonesList();
        createdUserData = callPostCustomerByPhonesList(phonesList);
        setCreatedUserPhoneAndId(createdUserData);
    }

    @Test(description = "Проверка списка свободных номеров, полученных методом 'getEmptyPhone'")
    public void getEmptyPhoneTest() {
        phonesList = getEmptyPhonesList();
        checkEmptyPhonesListByPostCustomer(phonesList);
    }

    @Test(description = "Проверка создания клиента методом 'postCustomer'", groups = "Общее предусловие")
    public void postCustomerTest() {
        checkCustomerCreation(getProperty("createdCustomerId"));
    }

    @Test(description = "Проверка активации клиента методом 'getCustomerById'", groups = "Общее предусловие")
    public void customerActivationTest() {
        String customerId = getProperty("createdCustomerId");
        checkCustomerStatus(customerId, "ACTIVE", 120);

        Customer data = getCustomerDataById(customerId);
        checkCustomerPassportData(data);
        checkCustomerAdditionalParameters(data);
    }

    @Test(description = "Проверка сохранения клиента в старой системе методом 'findByPhoneNumber'", groups = "Общее предусловие")
    public void findByPhoneNumberTest() {
        String oldSystemId = getCustomerIdFromOldSystem(getProperty("createdCustomerPhone"));
        checkCustomerIdFromOldSystem(getProperty("createdCustomerId"), oldSystemId);
    }

    @Test(description = "Проверка изменения статуса Aдминистратором", groups = "Общее предусловие")
    public void changeStatusByAdminTest() {
        String customerId = getProperty("createdCustomerId");
        callChangeCustomerStatus(customerId, "CHANGED");
        checkCustomerStatus(customerId, "CHANGED", 5);
    }

    @Test(description = "Проверка изменения статуса Пользователем", groups = "Общее предусловие")
    public void changeStatusByUserTest() {
        Response resp = callChangeCustomerStatus(getProperty("createdCustomerId"), "CHANGED");
        checkChangingStatusByUser(resp);
    }

}
