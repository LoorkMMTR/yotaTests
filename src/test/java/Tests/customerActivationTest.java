package Tests;

import models.Customer;
import org.testng.annotations.Test;
import services.BaseTest;

import static java.lang.System.getProperty;

public class customerActivationTest extends BaseTest {
    @Test(description = "Проверка активации клиента методом 'getCustomerById'", dataProvider = "parseUserToken")
    public void customerActivation(String user, String token) {
        phonesList = getEmptyPhonesList(token, 10);
        createdUserData = callPostCustomerByPhonesList(token, phonesList);
        setCreatedUserPhoneAndId(createdUserData);

        String customerId = getProperty("createdCustomerId");
        checkCustomerStatus(token, customerId, "ACTIVE", 120);
        Customer data = getCustomerDataById(token, customerId);
        checkCustomerPassportData(data.getPassportSeries(), data.getPassportNumber());
        checkCustomerAdditionalParameters(data.getAdditionalParameters().toString());
    }
}
