package Tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.qameta.allure.Feature;
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
        checkCustomerCreation(getProperty("createdCustomerId"), "NEW");
    }

    @Test(description = "Проверка поиска клиента по номеру методом 'findByPhoneNumber'", groups = "Общее предусловие")
    public void findByPhoneNumberTest() {
        getCustomerIdByPhone(getProperty("createdCustomerPhone"));
    }

    @Test(description = "Проверка корректности активации клиента", groups = "Общее предусловие")//TODO add status check
    public void checkCustomerData() {
        Customer data = getCustomerDataById(getProperty("createdCustomerId"));
        checkCustomerPassportData(data);
        checkCustomerAdditionalParameters(data);
    }

    @Test(description = "Проверка активации клиента", groups = "Общее предусловие")
    public void getCustomerActivationTest() {
        checkCustomerStatus(getProperty("createdCustomerId"), "ACTIVE", 120);
    }
}
