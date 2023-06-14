package org.Videos.repository.user;

public interface UserRepository {
    String insert = """
            insert into users(id,chat_id,firstname,lastname,phone_number,username,bio,state,created_time,updated_time)
            values(?::uuid,?,?,?,?,?,?,?,?,?);""";
    String getAllUsers = "select * from users;";
    String TRUNCATE_USERS = """
            truncate users;""";
    String update_users = "update users set state = ? where chat_id = ?;";
}
