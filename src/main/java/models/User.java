package models;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@Accessors
public class User {
    private String login;
    private String password;
    private String authToken;
}