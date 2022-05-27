package Tests;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import services.BaseTest;

import static java.lang.System.getProperty;

public class customerChangingStatusTest extends BaseTest {
    @Test(description = "Проверка изменения статуса Aдминистратором", dataProvider = "parseUserToken")
    public void changeStatus(String token, String user) {
        phonesList = getEmptyPhonesList(token, 10);
        createdUserData = callPostCustomerByPhonesList(token, phonesList);
        setCreatedUserPhoneAndId(createdUserData);

        if (user.equals("admin")) {
            String customerId = getProperty("createdCustomerId");
            callChangeCustomerStatus(token, customerId, "CHANGED");
            checkCustomerStatus(token, customerId, "CHANGED", 5);
        } else if (user.equals("user")) {
            Response resp = callChangeCustomerStatus(token, getProperty("createdCustomerId"), "CHANGED");
            checkChangingStatusByUser(resp);
        }
    }
}
