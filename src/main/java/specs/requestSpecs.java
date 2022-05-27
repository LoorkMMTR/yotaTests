package specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class requestSpecs {
    public static RequestSpecification JSONLog() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .log(LogDetail.BODY)
                .build();
    }
}
