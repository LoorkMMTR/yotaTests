package models;

import lombok.*;

@Data
@RequiredArgsConstructor
public class User {
    @NonNull
    private String login;
    @NonNull
    private String password;
    private String authToken;
}
