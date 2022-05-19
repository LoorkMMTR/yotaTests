package models;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@Accessors
//@With
//@JsonPOJOBuilder
//@AllArgsConstructor
//@NoArgsConstructor
public class User {
    private String login;
    private String password;
    private String authToken;
}