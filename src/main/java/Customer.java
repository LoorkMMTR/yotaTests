import lombok.*;
import lombok.experimental.Accessors;
import org.json.JSONObject;

@Data
@Accessors
public class Customer {
    private String name;
    private String phone;
    private JSONObject additionalParameters;
}