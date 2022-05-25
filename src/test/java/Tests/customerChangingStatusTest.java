package Tests;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import services.BaseTest;

import static java.lang.System.getProperty;

public class customerChangingStatusTest extends BaseTest {
    @Test(description = "Проверка изменения статуса Aдминистратором", groups = "Общее предусловие", dataProvider = "parseUserToken")
    public void changeStatus(String token, String user) {
        if (user.equals("admin")) {
            String customerId = getProperty("createdCustomerId");
            callChangeCustomerStatus(customerId, "CHANGED");
            checkCustomerStatus(token, customerId, "CHANGED", 5);
        } else if (user.equals("user")) {
            Response resp = callChangeCustomerStatus(getProperty("createdCustomerId"), "CHANGED");
            checkChangingStatusByUser(resp);
        }
    }
}
