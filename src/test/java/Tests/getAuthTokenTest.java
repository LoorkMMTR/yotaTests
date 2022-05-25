package Tests;

import org.testng.annotations.Test;
import services.BaseTest;

public class getAuthTokenTest extends BaseTest {
    @Test(dataProvider = "parseUserToken")
    public void getToken(String user, String token) {
        checkAuthToken(token);
    }
}
