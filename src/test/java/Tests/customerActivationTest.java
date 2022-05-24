package Tests;

import models.Customer;
import org.testng.annotations.Test;
import services.BaseTest;

import static java.lang.System.getProperty;

public class customerActivationTest extends BaseTest {
    @Test(description = "Проверка активации клиента методом 'getCustomerById'", groups = "Общее предусловие")
    public void customerActivation() {
        String customerId = getProperty("createdCustomerId");
        checkCustomerStatus(customerId, "ACTIVE", 120);

        Customer data = getCustomerDataById(customerId);
        checkCustomerPassportData(data);
        checkCustomerAdditionalParameters(data);
    }
}
