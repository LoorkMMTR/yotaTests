package Tests;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import services.BaseTest;

import static java.lang.System.getProperty;

public class customerChangingStatusTest extends BaseTest {
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
