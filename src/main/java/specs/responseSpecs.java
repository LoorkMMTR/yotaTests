package specs;

import io.restassured.filter.log.LogDetail;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.given;

public class responseSpecs {
    public static ResponseSpecification logsOnly() {
        return given()
                .when().then()
                .logDetail(LogDetail.BODY);
    }
}
