package lk.ijse.dto;

import lombok.*;

import java.io.InputStream;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserDto {

    private String username;
    private String password;
    private InputStream image;
}
