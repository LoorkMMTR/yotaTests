package Tests;

import org.testng.annotations.Test;
import services.BaseTest;

public class emptyPhonesListTest extends BaseTest {
    @Test(description = "Проверка списка свободных номеров, полученных методом 'getEmptyPhone'")
    public void getEmptyPhone() {
        phonesList = getEmptyPhonesList();
        checkEmptyPhonesListByPostCustomer(phonesList);
    }
}
