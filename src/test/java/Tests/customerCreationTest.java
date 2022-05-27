package Tests;

import org.testng.annotations.Test;
import services.BaseTest;

import static java.lang.System.getProperty;

public class customerCreationTest extends BaseTest {
    @Test(description = "Проверка создания клиента методом 'postCustomer'", dataProvider = "parseUserToken")
    public void postCustomer(String user, String token) {
        phonesList = getEmptyPhonesList(token, 10);
        createdUserData = callPostCustomerByPhonesList(token, phonesList);
        setCreatedUserPhoneAndId(user, createdUserData);

        checkCustomerCreation(token, getProperty(user+"CreatedCustomerId"));
    }
}
