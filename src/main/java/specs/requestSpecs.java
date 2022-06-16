package specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class requestSpecs {
    public static RequestSpecification JSONLog() {
        return new RequestSpecBuilder()
                .setBaseUri(System.getProperty("defaultURL")+":"+System.getProperty("defaultPort"))
                .setContentType(ContentType.JSON)
                .log(LogDetail.BODY)
                .build();
    }

    public static RequestSpecification BaseUrl() {
        return new RequestSpecBuilder()
                .setBaseUri(System.getProperty("defaultURL") + ":" + System.getProperty("defaultPort"))
                .build();
    }
}
