package Tests;

import org.testng.annotations.Test;
import services.BaseTest;

import static java.lang.System.getProperty;

public class oldSystemSavingTest extends BaseTest {
    @Test(description = "Проверка сохранения клиента в старой системе методом 'findByPhoneNumber'", dataProvider = "parseUserToken")
    public void findByPhoneNumberTest(String user, String token) {
        phonesList = getEmptyPhonesList(token, 10);
        createdUserData = callPostCustomerByPhonesList(token, phonesList);
        setCreatedUserPhoneAndId(createdUserData);

        String oldSystemId = getCustomerIdFromOldSystem(token, getProperty("createdCustomerPhone"));
        checkCustomerIdFromOldSystem(getProperty("createdCustomerId"), oldSystemId);
    }
}
