import org.testng.annotations.BeforeTest;

public class BaseTest implements Requests, Steps {
    @BeforeTest(alwaysRun = true)
    public void setDefaultBasePath() {
        setBasePath();
    }

    @BeforeTest
    public void getToken() {
        getAuthToken();
    }
}
