package Tests;

import org.testng.annotations.Test;
import services.BaseTest;

import static java.lang.System.getProperty;

public class customerCreationTest extends BaseTest {
    @Test(description = "Проверка создания клиента методом 'postCustomer'", groups = "Общее предусловие", dataProvider = "parseUserToken")
    public void postCustomer(String user, String token) {
        phonesList = getEmptyPhonesList(token);
        createdUserData = callPostCustomerByPhonesList(token, phonesList);
        setCreatedUserPhoneAndId(createdUserData);
        checkCustomerCreation(token, getProperty("createdCustomerId"));
    }
}
