package services;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import steps.Steps;

import java.util.List;

public class BaseTest implements Steps {
    protected List<Long> phonesList;
    protected List<String> createdUserData;

    @BeforeTest(alwaysRun = true)
    public void setDefaultBasePath() {
        setBasePath();
    }

    @BeforeTest
    public void getToken() {
        System.setProperty("authToken", getAuthToken());
    }

    @BeforeMethod(description = "Создание клиента", onlyForGroups = "Общее предусловие")
    public void createCustomer() {
        phonesList = getEmptyPhonesList();
        createdUserData = callPostCustomerByPhonesList(phonesList);
        setCreatedUserPhoneAndId(createdUserData);
    }
}
