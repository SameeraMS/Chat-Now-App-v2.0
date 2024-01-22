package lk.ijse.model;

import lk.ijse.dto.UserDto;
import lk.ijse.util.SqlUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserModel {
    public static boolean existsUser(String username) throws SQLException {
        String query = "SELECT username FROM user WHERE username = ?";
        ResultSet rs = SqlUtil.execute(query,username);
        return rs.next();
    }

    public static boolean saveUser(UserDto userDto) throws SQLException {
        String query = "INSERT INTO user VALUES (?,?,?)";
        return SqlUtil.execute(query,userDto.getUsername(),userDto.getPassword(),userDto.getImage());

    }

    public static UserDto userDetails(String username) throws SQLException {
        String query = "SELECT * FROM user WHERE username = ?";
        ResultSet rs = SqlUtil.execute(query,username);
        if (rs.next()){
            return new UserDto(rs.getString(1),rs.getString(2),rs.getBinaryStream(3));
        } else {
            return null;
        }
    }
}
