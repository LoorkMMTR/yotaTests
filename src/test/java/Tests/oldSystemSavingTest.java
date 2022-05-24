package Tests;

import org.testng.annotations.Test;
import services.BaseTest;

import static java.lang.System.getProperty;

public class oldSystemSavingTest extends BaseTest {
    @Test(description = "Проверка сохранения клиента в старой системе методом 'findByPhoneNumber'", groups = "Общее предусловие")
    public void findByPhoneNumberTest() {
        String oldSystemId = getCustomerIdFromOldSystem(getProperty("createdCustomerPhone"));
        checkCustomerIdFromOldSystem(getProperty("createdCustomerId"), oldSystemId);
    }
}
