package Tests;

import models.Customer;
import org.testng.annotations.Test;
import services.BaseTest;

import static java.lang.System.getProperty;

public class customerActivationTest extends BaseTest {
    @Test(description = "Проверка активации клиента методом 'getCustomerById'",
            groups = "Общее предусловие",
            dataProvider = "parseUserToken")
    public void customerActivation(String user, String token) {
        String customerId = getProperty("createdCustomerId");
        checkCustomerStatus(token, customerId, "ACTIVE", 120);

        Customer data = getCustomerDataById(token, customerId);
        checkCustomerPassportData(data);
        checkCustomerAdditionalParameters(data);
    }
}
