package services;

import io.restassured.RestAssured;
import models.User;
import org.testng.ITestContext;
import org.testng.annotations.*;
import steps.Steps;

import java.util.List;

import static java.lang.Integer.parseInt;
import static java.lang.System.getProperty;
import static java.lang.System.setProperty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;

public class BaseTest implements Steps {
    protected List<Long> phonesList;
    protected List<String> createdUserData;

    @DataProvider(parallel = true)
    public Object[][] parseUserToken() {
        return new Object[][]{
                {"admin", getProperty("authAdminToken")},
                {"user", getProperty("authUserToken")},
        };
    }

    @BeforeTest(description = "Установка дефолтных параметров")
    public void setDefaultBasePath() {
        RestAssured.baseURI = getProperty("defaultURL");
        RestAssured.port = parseInt(getProperty("defaultPort"));
    }

    @BeforeTest(description = "Получение токенов авторизации")
    public void getTokens() {
        setProperty("authAdminToken", getAuthToken(true));
        setProperty("authUserToken", getAuthToken(false));
    }

//    @Test(dataProvider = "parseUserToken")
//    @BeforeMethod(description = "Создание клиента", onlyForGroups = "Общее предусловие")
//    public void createCustomer(String user, String token) {
//        phonesList = getEmptyPhonesList(token);
//        createdUserData = callPostCustomerByPhonesList(token, phonesList);
//        setCreatedUserPhoneAndId(createdUserData);
//    }
}
