package dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder

public class UserDtoLombok {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
}
