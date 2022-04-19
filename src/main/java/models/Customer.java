package models;

import com.fasterxml.jackson.core.JsonParser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.openqa.selenium.json.Json;

@Data
@RequiredArgsConstructor
public class Customer {
    @NonNull
    private String name;
    @NonNull
    private String phone;
    @NonNull
    private JSONObject additionalParameters;

    private String customerId;
    private String status;
    private String pd;
}