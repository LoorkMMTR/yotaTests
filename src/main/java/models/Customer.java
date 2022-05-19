package models;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@Accessors
public class Customer{
    private String customerId;
    private String name;
    private String status;
    private String phone;
    public AdditionalParameters additionalParameters;
    private String pd;
    private String passportNumber;
    private String passportSeries;

    @Data
    @Accessors
    public static class AdditionalParameters{
        private String string;
    }
}