package lk.ijse.model;

import lk.ijse.dto.ChatDto;
import lk.ijse.util.SqlUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChatModel {

    public boolean saveChat(ChatDto dto) throws SQLException {
        String query = "INSERT INTO chat VALUES (?,?,?,?,?)";
        return SqlUtil.execute(query,dto.getUsername(),dto.getId(),dto.getMessage(),dto.getImage(),dto.getTime());

    }

    public ArrayList<ChatDto> getChat(String username) throws SQLException {
        String query = "SELECT * FROM chat WHERE username = ? ORDER BY time";
        ResultSet rs = SqlUtil.execute(query,username);
        ArrayList<ChatDto> list = new ArrayList<>();
        while (rs.next()){
            list.add(new ChatDto(rs.getString(1),rs.getString(2),
                    rs.getString(3),rs.getBinaryStream(4),rs.getString(5)));
        }
        return list;
    }
}
