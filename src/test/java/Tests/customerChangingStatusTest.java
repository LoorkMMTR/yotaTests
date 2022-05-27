package Tests;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import services.BaseTest;

import java.util.Objects;

import static java.lang.System.getProperty;

public class customerChangingStatusTest extends BaseTest {
    @Test(description = "Проверка изменения статуса", dataProvider = "parseUserToken")
    public void changeStatus(String user, String token) {
        phonesList = getEmptyPhonesList(token, 10);
        createdUserData = callPostCustomerByPhonesList(token, phonesList);
        setCreatedUserPhoneAndId(user, createdUserData);

        if (Objects.equals(user, "admin")) {
            String customerId = getProperty(user+"CreatedCustomerId");
            callChangeCustomerStatus(token, customerId, "CHANGED");
            checkCustomerStatus(token, customerId, "CHANGED", 5);
        } else if (user.equals("user")) {
            Response resp = callChangeCustomerStatus(token, getProperty(user+"CreatedCustomerId"), "CHANGED");
            checkChangingStatusByUser(resp);
        }
    }
}
