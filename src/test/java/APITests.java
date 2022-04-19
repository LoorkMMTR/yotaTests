import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static steps.Steps.*;

@Feature("Тестовое задание для Yota")
public class APITests {

    @BeforeClass(description = "Установка параметров 'baseURI' и 'port'", alwaysRun = true)
    public void setBasePath() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test(description = "Проверка метода 'login'")
    public void checkLogin() {
        getAuthToken(true);
        checkAuthToken();
    }

    @Test(description = "Проверка метода 'getEmptyPhone'", dependsOnMethods = "checkLogin")
    public void checkGetEmptyPhone() {
        getEmptyPhoneNumber();
        checkEmptyPhoneNumber();
    }

    @Test(description = "Проверка метода 'postCustomer'", dependsOnMethods = {"checkLogin","checkGetEmptyPhone"})
    public void checkPostCustomer() {
        postCustomer();
        checkPostCustomerCreation();
    }


}
