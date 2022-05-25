package Tests;

import org.testng.annotations.Test;
import services.BaseTest;

public class emptyPhonesListTest extends BaseTest {
    @Test(description = "Проверка списка свободных номеров, полученных методом 'getEmptyPhone'",
    dataProvider = "parseUserToken")
    public void getEmptyPhone(String user, String token) {
        phonesList = getEmptyPhonesList(token);
        checkEmptyPhonesListByPostCustomer(token, phonesList);
    }
}
