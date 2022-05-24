package Tests;

import org.testng.annotations.Test;
import services.BaseTest;

import static java.lang.System.getProperty;

public class customerCreationTest extends BaseTest {
    @Test(description = "Проверка создания клиента методом 'postCustomer'", groups = "Общее предусловие")
    public void postCustomer() {
        checkCustomerCreation(getProperty("createdCustomerId"));
    }
}
