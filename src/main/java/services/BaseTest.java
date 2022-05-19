package services;

import org.testng.annotations.BeforeTest;
import steps.Steps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class BaseTest implements Steps {
    @BeforeTest(alwaysRun = true)
    public void setDefaultBasePath() {
        setBasePath();
    }

    @BeforeTest
    public void getToken() {
        String authToken = getAuthToken();
        assertThat("FAIL: Токен не получен", authToken, not(emptyOrNullString()));
        System.setProperty("authToken", authToken);
    }
}
