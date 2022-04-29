import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;

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
    private String errorMsgText;
}