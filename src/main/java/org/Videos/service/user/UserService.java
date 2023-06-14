package org.Videos.service.user;

import org.Videos.model.user.User;
import org.Videos.model.user.UserState;
import org.Videos.repository.user.UserRepositoryImpl;
import org.telegram.telegrambots.meta.api.objects.Contact;

import java.util.ArrayList;
import java.util.Objects;

public class UserService {
    private static final UserService instance = new UserService();
    private static final UserRepositoryImpl userRepo = UserRepositoryImpl.getInstance();
    private UserService() {
    }

    public static UserService getInstance() {
        return instance;
    }
    public User getByChatId(Long chatId){
        for(User user: userRepo.getAllUsers()){
            if(Objects.equals(user.getChatId(),chatId)) return user;
        }
        return null;
    }
    public boolean addUser(Contact contact, String bio, String username){
        if(checkPhoneNumber(contact.getPhoneNumber())) {
            return false;
        }

        User user = new User()
                .setState(UserState.REGISTERED)
                .setFirstName(contact.getFirstName())
                .setLastName(contact.getLastName())
                .setPhoneNumber(contact.getPhoneNumber())
                .setBio(bio)
                .setUsername(username)
                .setChatId(contact.getUserId());
        userRepo.save(user);
        return true;
    }

    public boolean checkPhoneNumber(String phoneNumber){
        for(User user: userRepo.getAllUsers()){
            if(Objects.equals(user.getPhoneNumber(),phoneNumber)) return true;
        }
        return false;
    }
    public void updateState(Long chatId,UserState state){
        ArrayList<User> users = userRepo.getAllUsers();

        for(User user: users){
            if(Objects.equals(user.getChatId(),chatId)) {
                userRepo.updateState(chatId.toString(),state.toString());
            }
        }
    }

}
