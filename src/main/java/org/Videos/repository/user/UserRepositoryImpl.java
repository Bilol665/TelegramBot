package org.Videos.repository.user;


import org.Videos.model.user.User;
import org.Videos.utils.Utils;

import java.sql.*;
import java.util.ArrayList;

public class UserRepositoryImpl implements UserRepository {
    private static final UserRepositoryImpl instance = new UserRepositoryImpl();
    private UserRepositoryImpl() {
    }
    public static UserRepositoryImpl getInstance() {
        return instance;
    }
    private static final Connection connection = Utils.connection();

    public boolean save(User user){

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insert);

            preparedStatement.setString(1, String.valueOf(user.getId()));
            preparedStatement.setString(2, String.valueOf(user.getChatId()));
            preparedStatement.setString(3,user.getFirstName());
            preparedStatement.setString(4,user.getLastName());
            preparedStatement.setString(5,user.getPhoneNumber());
            preparedStatement.setString(6,user.getUsername());
            preparedStatement.setString(7,user.getBio());
            preparedStatement.setString(8, String.valueOf(user.getState()));
            preparedStatement.setTimestamp(9, Timestamp.valueOf(user.getCreated_time()));
            preparedStatement.setTimestamp(10, Timestamp.valueOf(user.getUpdated_time()));

            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
            return false;
        }
    }
    public boolean updateState(String chatId,String state){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(update_users);

            preparedStatement.setString(1,state);
            preparedStatement.setString(2,chatId);

            preparedStatement.execute();

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public ArrayList<User> getAllUsers(){
        ArrayList<User> users = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(getAllUsers);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                users.add(User.map(resultSet));
            }
            return users;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
