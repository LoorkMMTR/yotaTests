package Tests;

import api.Requests;
import org.testng.annotations.Test;
import steps.Steps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;

public class getAuthTokenTest implements Requests, Steps {
    @Test()
    public void getToken() {
        String authToken = getAuthToken();
        assertThat("Токен не получен", authToken, not(emptyOrNullString()));
        System.setProperty("authToken", authToken);
    }
}
