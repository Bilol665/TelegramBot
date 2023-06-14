package org.Videos.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.Videos.model.BaseModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class User extends BaseModel {
    private Long chatId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String username;
    private String bio;
    private UserState state;

    public static User map(ResultSet resultSet){
        try {
            User user = new User()
                    .setChatId(resultSet.getLong("chat_id"))
                    .setFirstName(resultSet.getString("firstname"))
                    .setLastName(resultSet.getString("lastname"))
                    .setPhoneNumber(resultSet.getString("phone_number"))
                    .setUsername(resultSet.getString("username"))
                    .setBio(resultSet.getString("bio"))
                    .setState(UserState.valueOf(resultSet.getString("state")));
            user.setId(UUID.fromString(resultSet.getString("id")));
            user.setCreated_time(resultSet.getTimestamp("created_time").toLocalDateTime());
            user.setUpdated_time(resultSet.getTimestamp("updated_time").toLocalDateTime());

            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

