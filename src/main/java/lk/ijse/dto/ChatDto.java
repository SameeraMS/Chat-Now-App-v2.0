package lk.ijse.dto;

import lombok.*;

import java.io.InputStream;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChatDto {
    private String username;
    private String id;
    private String message;
    private InputStream image;
    private String time;
}
