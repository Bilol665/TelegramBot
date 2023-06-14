package org.Videos;

import lombok.SneakyThrows;
import org.Videos.model.user.User;
import org.Videos.model.user.UserState;
import org.Videos.service.bot.BotService;
import org.Videos.service.movie.MovieService;
import org.Videos.service.user.UserService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.*;

import java.util.Objects;

public class GamesLibrary extends TelegramLongPollingBot {
    static UserService userService = UserService.getInstance();
    static BotService botService = BotService.getInstance();
    static MovieService movieService = MovieService.getInstance();
    public static String searchText = "1";
    public static String searchText2 = "1";
    static Boolean genres = false;
    static Boolean searchName = false;
    static Boolean categories = false;
    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        Long chatId;
        String text;
        if (message != null) {
            chatId = message.getChatId();
            text = message.getText();

        }else {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            text = update.getCallbackQuery().getMessage().getText();
        }
        if(botService.isCommand(text)){
            searchText2 = "-1";
            searchText = "1";
            genres = false;
            searchName = false;
        }
        User currentUser = userService.getByChatId(chatId);
        System.out.println(chatId);
        if (currentUser == null) {
            if (message.hasContact()) {
                System.out.println("hhhh");
                Chat chat = message.getChat();
                Contact contact = message.getContact();
                userService.addUser(contact, chat.getBio(), chat.getUserName());
                execute(botService.welcomeMessage(chatId.toString()));
            } else {
                execute(botService.requestContactMessage(chatId.toString()));
            }
        } else {
            if (update.hasCallbackQuery()) {
                if (genres) {
                    if (!movieService.isOnlyNumber(update.getCallbackQuery().getData())) {

                        switch (update.getCallbackQuery().getData()) {
                            case "next" -> {
                                CallbackQuery callbackQuery = update.getCallbackQuery();
                                Integer messageId = callbackQuery.getMessage().getMessageId();
                                String inlineMessageId = callbackQuery.getInlineMessageId();
                                botService.i++;
                                execute(botService.stages(chatId, messageId, inlineMessageId));
                            }
                            case "back" -> {
                                CallbackQuery callbackQuery = update.getCallbackQuery();
                                Integer messageId = callbackQuery.getMessage().getMessageId();
                                String inlineMessageId = callbackQuery.getInlineMessageId();
                                botService.i--;
                                execute(botService.stages(chatId, messageId, inlineMessageId));
                            }
                            default -> {
                                String genre = update.getCallbackQuery().getData();
                                if (!movieService.checkForZero(genre)) {
                                    execute(new SendMessage(chatId.toString(), "Nothing found"));
                                    return;
                                }
                                execute(botService.resultGenres(genre, chatId.toString()));
                                execute(botService.handleButtonClick(update.getCallbackQuery()));
                                System.out.println(genre);
                                System.out.println("line 83");
                                genres = true;
                            }
                        }
                        return;
                    } else {
                        searchText2 = movieService.getById(Integer.valueOf(update.getCallbackQuery().getData())).getTitle();
                    }
                }
            }

                if ((searchName && searchText.length() > 2)) {
                    if(update.getCallbackQuery().getData().startsWith("back")) {
                        String str = update.getCallbackQuery().getData().substring(3);
                        searchText = movieService.getById(Integer.valueOf(str)).getTitle();
                    }
                    if (update.hasCallbackQuery() && !Objects.equals(update.getCallbackQuery().getData(), searchText)) {
                        CallbackQuery callbackQuery = update.getCallbackQuery();
                        callbackQuery.getMessage().getChatId();

                        DeleteMessage deleteMessage = botService.handleButtonClick(callbackQuery);
                        SendPhoto sendPhoto = botService.sendMessageAfterSearch(update.getCallbackQuery());
                        execute(deleteMessage);
                        execute(sendPhoto);
                        System.out.println(update.getCallbackQuery().getData());
                        return;
                    } else if (Objects.equals(update.getCallbackQuery().getData(), searchText)) {
                        CallbackQuery callbackQuery = update.getCallbackQuery();
                        callbackQuery.getMessage().getChatId();

                        DeleteMessage deleteMessage = botService.handleButtonClick(callbackQuery);

                        execute(deleteMessage);
                        execute(botService.resultGenres(searchText2, chatId.toString()));
                        System.out.println("result in if-case");
                        return;
                    }
                }
                if (genres && !searchText2.equals("-1")) {
                    if(update.getCallbackQuery().getData().startsWith("back")) {
                        String str = update.getCallbackQuery().getData().substring(3);
                        searchText2 = movieService.getById(Integer.valueOf(str)).getTitle();
                    }
                    if (update.hasCallbackQuery() && !Objects.equals(update.getCallbackQuery().getData(), searchText2)) {
                        CallbackQuery callbackQuery = update.getCallbackQuery();
                        callbackQuery.getMessage().getChatId();

                        DeleteMessage deleteMessage = botService.handleButtonClick(callbackQuery);
                        SendPhoto sendPhoto = botService.sendMessageAfterSearch(update.getCallbackQuery());
                        execute(deleteMessage);
                        execute(sendPhoto);
                        System.out.println(update.getCallbackQuery().getData());
                        return;
                    } else if (Objects.equals(update.getCallbackQuery().getData(), searchText2)) {
                        CallbackQuery callbackQuery = update.getCallbackQuery();
                        callbackQuery.getMessage().getChatId();

                        DeleteMessage deleteMessage = botService.handleButtonClick(callbackQuery);

                        execute(deleteMessage);
                        execute(botService.resultGenres(searchText2, chatId.toString()));
                        System.out.println("result in if-case");
                        return;
                    }

                }

            switch (text) {
                case "/start" -> {
                    execute(botService.welcomeBackMessage(chatId.toString()));
                    currentUser.setState(UserState.REGISTERED);
                    userService.updateState(chatId, UserState.REGISTERED);
                    botService.i = 1;
                    return;
                }
                case "All genres" -> {
                    genres = true;
                    execute(botService.genresMessage(chatId.toString()));
                    currentUser.setState(UserState.ALL_GENRES);
                    userService.updateState(chatId,UserState.ALL_GENRES);
                    return;
                }
                case "Categories" -> {

                    currentUser.setState(UserState.CATEGORIES);
                    userService.updateState(chatId,UserState.CATEGORIES);
                    return;
                }
                case "Search by name" -> {
                    searchName = true;
                    execute(botService.search(chatId.toString()));
                    currentUser.setState(UserState.SEARCH);
                    userService.updateState(chatId,UserState.SEARCH);
                    return;
                }
            }
            switch (currentUser.getState()) {
                case SEARCH -> {
                    searchName = true;
                    searchText = text;
                    execute(botService.result(text,chatId.toString()));
                    System.out.println("result in getState");
                    return;
                }
                case ALL_GENRES -> {
                    genres = true;
                    searchText2 = movieService.getById(Integer.valueOf(update.getCallbackQuery().getData())).getTitle();
                    System.out.println(movieService.getById(Integer.valueOf(update.getCallbackQuery().getData())).getTitle());
                    execute(botService.resultGenres(text,chatId.toString()));
                    System.out.println("Hello world");
                    return;
                }
                case SEARCH_BY_CATEGORY -> {

                }
            }
        }



    }

    @Override
    public String getBotUsername() {
        return "JustLikeMoviebot";
    }

    @Override
    public String getBotToken() {
        return "5881407963:AAH4JeT3SnhA8aJiSp0GdDLEkbcpINFi9us";
    }
}
