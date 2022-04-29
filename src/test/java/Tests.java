import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

@Feature("Тестовое задание для Yota")
public class Tests extends BaseTest implements Requests{

    @Test(description = "Проверка метода 'getEmptyPhone'")
    public void getEmptyPhoneTest() {
        List<Long> numbersList = getEmptyPhonesList();
        checkEmptyPhonesList(numbersList);
    }

    @Test(description = "Проверка метода 'postCustomer'")
    public void postCustomerTest() {
        List<Long> numbersList = getEmptyPhonesList();
        checkEmptyPhonesList(numbersList);
        String singlePhone = numbersList.get(0).toString();
        callPostCustomer(singlePhone);
        checkFindByPhoneNumber(singlePhone);
    }

    @Test(description = "Проверка метода 'findByPhoneNumber'")
    public void findByPhoneNumberTest() {
        String singlePhone = getEmptyPhonesList().get(0).toString();
        callPostCustomer(singlePhone);
        checkFindByPhoneNumber(singlePhone);
    }




//
//    @Flaky//errorMessage: Данный номер телефона уже используется
//    @Test(description = "Проверка метода 'postCustomer'", dependsOnMethods = {"checkLogin","checkGetEmptyPhone"})
//    public void checkPostCustomer() {
//        postCustomer();
//        checkPostCustomerCreation();
//    }


}
