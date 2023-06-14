package org.Videos.service.bot;

import org.Videos.GamesLibrary;
import org.Videos.model.movie.Movie;
import org.Videos.service.movie.MovieService;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BotService {
    public int i=1;
    private static final BotService instance = new BotService();
    private final MovieService movieService = MovieService.getInstance();
    private BotService() {
    }
    public static BotService getInstance() {
        return instance;
    }
    public SendMessage welcomeBackMessage(String chatId){
        SendMessage sendMessage = new SendMessage(chatId,"Welcome back!");
        sendMessage.setReplyMarkup(starterButtons());
        return sendMessage;
    }

    public ReplyKeyboardRemove removeReplyKeyboard() {
        ReplyKeyboardRemove remove = new ReplyKeyboardRemove();
        remove.setRemoveKeyboard(true);
        return remove;
    }
    public SendMessage welcomeMessage(String chatId){
        SendMessage sendMessage = new SendMessage(chatId,"Welcome");
        sendMessage.setReplyMarkup(starterButtons());
        return sendMessage;
    }

    private ReplyKeyboard starterButtons() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("All genres");
        row.add("Categories");
        rows.add(row);

        row = new KeyboardRow();
        row.add("Search by name");
        rows.add(row);

        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }


    public SendMessage search(String chatId){
        SendMessage sendMessage = new SendMessage(chatId,"Enter text to search");
        sendMessage.setReplyMarkup(removeReplyKeyboard());
        return sendMessage;
    }
    public SendMessage requestContactMessage(String chatId){
        SendMessage sendMessage = new SendMessage(chatId,"Please share your number📲");
        sendMessage.setReplyMarkup(requestContactButton());
        return sendMessage;
    }

    private ReplyKeyboardMarkup requestContactButton() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton("Share phone number");
        button.setRequestContact(true);
        row.add(button);

        replyKeyboardMarkup.setKeyboard(List.of(row));
        return replyKeyboardMarkup;
    }
    public SendMessage result(String name,String chatId) {
        SendMessage sendMessage = new SendMessage(chatId,"Here are your results");
        sendMessage.setReplyMarkup(movieService.searchByNameInlineKeyboard(name));
        return sendMessage;
    }
    public SendMessage resultGenres(String genre,String chatId) {
        SendMessage sendMessage = new SendMessage(chatId,"Here are your results");
        sendMessage.setReplyMarkup(movieService.searchByGenreInlineKeyboard(genre));
        return sendMessage;
    }
    public Boolean isCommand(String command) {
        return Objects.equals(command, "/start") || Objects.equals(command, "All genres")
                || Objects.equals(command, "Categories") || Objects.equals(command, "Search by name");
    }
    public DeleteMessage handleButtonClick(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        int messageId = callbackQuery.getMessage().getMessageId();

        return new DeleteMessage(chatId.toString(), messageId);
    }
    public SendPhoto sendMessageAfterSearch(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();

        Movie movie = movieService.getById(Integer.valueOf(callbackQuery.getData()));
        InputFile inputFile = new InputFile(movie.getPostUrl());

        SendPhoto sendPhoto = new SendPhoto(chatId.toString(),inputFile);
        String slogan;
        if (movie.getSlogan() == null) {
            slogan = "";
        }else{
            slogan = movie.getSlogan();
        }
        String caption = movie.getTitle() + "\n\n" + slogan + "\n\n" + movie.getPlot();
        sendPhoto.setCaption(caption);
        sendPhoto.setReplyMarkup(setToDescription(movie));

        return sendPhoto;
    }
    public EditMessageReplyMarkup stages(Long chatId,Integer messageId,String inlineMessageId){
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setChatId(chatId.toString());
        editMessageReplyMarkup.setMessageId(messageId);
        editMessageReplyMarkup.setInlineMessageId(inlineMessageId);
        switch (i) {
            case 1 -> editMessageReplyMarkup.setReplyMarkup(genres());
            case 2 -> editMessageReplyMarkup.setReplyMarkup(genres2());
            case 3 -> editMessageReplyMarkup.setReplyMarkup(genres3());
            case 4 -> editMessageReplyMarkup.setReplyMarkup(genres4());
            case 5 -> editMessageReplyMarkup.setReplyMarkup(genres5());
        }
        return editMessageReplyMarkup;
    }
    public InlineKeyboardMarkup setToDescription(Movie movie) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Watch");
        button.setUrl(movie.getUrl());
        row.add(button);
        rows.add(row);
        row = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Back");
        button1.setCallbackData("back"+movie.getId().toString());
        row.add(button1);
        rows.add(row);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }
    public SendMessage genresMessage(String chatId){
        SendMessage sendMessage = new SendMessage(chatId,"Genres:");
        sendMessage.setReplyMarkup(genres());
        return sendMessage;
    }


    public InlineKeyboardMarkup genres(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Аниме");
        button.setCallbackData("аниме");
        row.add(button);

        button = new InlineKeyboardButton();
        button.setText("Биография");
        button.setCallbackData("биография");
        row.add(button);

        button = new InlineKeyboardButton();
        button.setText("Боевик");
        button.setCallbackData("боевик");
        row.add(button);

        rows.add(row);
        row = new ArrayList<>();

        button = new InlineKeyboardButton();
        button.setText("Вестерн");
        button.setCallbackData("вестерн");
        row.add(button);

        button = new InlineKeyboardButton();
        button.setText("Военный");
        button.setCallbackData("военный");
        row.add(button);

        button = new InlineKeyboardButton();
        button.setText("Детектив");
        button.setCallbackData("детектив");
        row.add(button);

        rows.add(row);
        row = new ArrayList<>();

        button = new InlineKeyboardButton();
        button.setText("Детский");
        button.setCallbackData("детский");
        row.add(button);

        button = new InlineKeyboardButton();
        button.setText("Документальный");
        button.setCallbackData("документальный");
        row.add(button);

        button = new InlineKeyboardButton();
        button.setText("Драма");
        button.setCallbackData("драма");
        row.add(button);

        rows.add(row);
        row = new ArrayList<>();

        button = new InlineKeyboardButton();
        button.setText("Игра");
        button.setCallbackData("игра");
        row.add(button);

        button = new InlineKeyboardButton();
        button.setText("Играфильм");
        button.setCallbackData("играфильм");
        row.add(button);

        button = new InlineKeyboardButton();
        button.setText("Исскуство");
        button.setCallbackData("исскуство");
        row.add(button);

        rows.add(row);
        row = new ArrayList<>();

        button = new InlineKeyboardButton();
        button.setText("История");
        button.setCallbackData("история");
        row.add(button);

        button = new InlineKeyboardButton();
        button.setText("Киберспотр");
        button.setCallbackData("киберспотр");
        row.add(button);

        button = new InlineKeyboardButton();
        button.setText("Комедия");
        button.setCallbackData("комедия");
        return getInlineKeyboardMarkup(inlineKeyboardMarkup, rows, row, button);
    }

    @NotNull
    private InlineKeyboardMarkup getInlineKeyboardMarkup(InlineKeyboardMarkup inlineKeyboardMarkup, List<List<InlineKeyboardButton>> rows, List<InlineKeyboardButton> row, InlineKeyboardButton button) {
        row.add(button);

        rows.add(row);
        row = new ArrayList<>();

        button = new InlineKeyboardButton();
        button.setText("<-");
        button.setCallbackData("back");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText(i + "/5");
        button.setCallbackData("hhh");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("->");
        button.setCallbackData("next");
        row.add(button);
        rows.add(row);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup genres4(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton();

        button.setText("Фэнтези");
        button.setCallbackData("Фэнтези");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Церемония");
        button.setCallbackData("Церемония");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Экшен");
        button.setCallbackData("Экшен");
        row.add(button);

        rows.add(row);
        row = new ArrayList<>();

        button = new InlineKeyboardButton();
        button.setText("Юмор");
        button.setCallbackData("Юмор");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Скетч-шоу");
        button.setCallbackData("Скетч-шоу");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Оточоственный");
        button.setCallbackData("Оточоственный");
        row.add(button);

        rows.add(row);
        row = new ArrayList<>();

        button = new InlineKeyboardButton();
        button.setText("Исторический");
        button.setCallbackData("Исторический");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Семейный");
        button.setCallbackData("Семейный");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Культура");
        button.setCallbackData("Культура");
        row.add(button);

        rows.add(row);
        row = new ArrayList<>();

        button = new InlineKeyboardButton();
        button.setText("Экономика");
        button.setCallbackData("Экономика");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Социология");
        button.setCallbackData("Социология");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Политика");
        button.setCallbackData("Политика");
        row.add(button);

        rows.add(row);
        row = new ArrayList<>();

        button = new InlineKeyboardButton();
        button.setText("Экранизация");
        button.setCallbackData("Экранизация");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("музыкальный");
        button.setCallbackData("музыкальный");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("медицина");
        button.setCallbackData("медицина");
        return getInlineKeyboardMarkup(inlineKeyboardMarkup, rows, row, button);

    }
    public InlineKeyboardMarkup genres2(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Концерт");
        button.setCallbackData("концерт");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Короткометражка");
        button.setCallbackData("короткометражка");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Криминал");
        button.setCallbackData("криминал");
        row.add(button);

        rows.add(row);
        row = new ArrayList<>();

        button = new InlineKeyboardButton();
        button.setText("Кулинарное шоу");
        button.setCallbackData("кулинарное шоу");
        row.add(button);

        button = new InlineKeyboardButton();
        button.setText("Мелодрамма");
        button.setCallbackData("мелодрамма");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Мистика");
        button.setCallbackData("мистика");
        row.add(button);

        rows.add(row);
        row = new ArrayList<>();

        button = new InlineKeyboardButton();
        button.setText("Музыка");
        button.setCallbackData("музыка");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Мультфильм");
        button.setCallbackData("мультфильм");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Мюзикл");
        button.setCallbackData("мюзикл");
        row.add(button);

        rows.add(row);
        row = new ArrayList<>();
        button = new InlineKeyboardButton();
        button.setText("Научно-познавательный");
        button.setCallbackData("научно-познавательный");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Научно-популярный");
        button.setCallbackData("научно-популярный");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Обзор");
        button.setCallbackData("обзор");

        rows.add(row);
        row = new ArrayList<>();

        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Обучающее видео");
        button.setCallbackData("обучающее видео");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Породия");
        button.setCallbackData("породия");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Познавательный");
        button.setCallbackData("познавательный");
        return getInlineKeyboardMarkup(inlineKeyboardMarkup, rows, row, button);
    }
    public InlineKeyboardMarkup genres3(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Приключения");
        button.setCallbackData("приключения");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Природа");
        button.setCallbackData("природа");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Развлекательное шоу");
        button.setCallbackData("развлекательное шоу");
        row.add(button);

        rows.add(row);
        row = new ArrayList<>();
        button = new InlineKeyboardButton();
        button.setText("Реалити Шоу");
        button.setCallbackData("реалити Шоу");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Реальное Тв");
        button.setCallbackData("реальное Тв");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Романтика");
        button.setCallbackData("романтика");
        row.add(button);

        rows.add(row);
        row = new ArrayList<>();
        button = new InlineKeyboardButton();
        button.setText("Семейный");
        button.setCallbackData("семейный");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Спорт");
        button.setCallbackData("спорт");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("ТВ-шоу");
        button.setCallbackData("тв-шоу");
        row.add(button);

        rows.add(row);
        row = new ArrayList<>();
        button = new InlineKeyboardButton();
        button.setText("Телепередача");
        button.setCallbackData("телепередача");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Ток-шоу");
        button.setCallbackData("ток-шоу");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Триллер");
        button.setCallbackData("триллер");
        row.add(button);

        rows.add(row);
        row = new ArrayList<>();
        button = new InlineKeyboardButton();
        button.setText("Ужасы");
        button.setCallbackData("ужасы");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Фантастика");
        button.setCallbackData("фантастика");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Фильмнуар");
        button.setCallbackData("фильмнуар");
        return getInlineKeyboardMarkup(inlineKeyboardMarkup, rows, row, button);
    }
    public InlineKeyboardMarkup genres5(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton();
        button = new InlineKeyboardButton();
        button.setText("катастрофа");
        button.setCallbackData("катастрофа");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Кулинарный");
        button.setCallbackData("Кулинарный");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Обучающий");
        button.setCallbackData("Обучающий");
        row.add(button);

        rows.add(row);
        row = new ArrayList<>();

        button = new InlineKeyboardButton();
        button.setText("расследование");
        button.setCallbackData("расследование");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Короткометражный");
        button.setCallbackData("Короткометражный");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Короткометражный фильм");
        button.setCallbackData("Короткометражный фильм");
        row.add(button);

        rows.add(row);
        row = new ArrayList<>();

        button = new InlineKeyboardButton();
        button.setText("Сериал");
        button.setCallbackData("Сериал");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("комеди");
        button.setCallbackData("комеди");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("наука");
        button.setCallbackData("наука");
        row.add(button);

        rows.add(row);
        row = new ArrayList<>();

        button = new InlineKeyboardButton();
        button.setText("новости");
        button.setCallbackData("новости");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Дания");
        button.setCallbackData("Дания");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Швеция");
        button.setCallbackData("Швеция");
        row.add(button);

        rows.add(row);
        row = new ArrayList<>();

        button = new InlineKeyboardButton();
        button.setText("Музыкальные");
        button.setCallbackData("Музыкальные");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Мультфильмы");
        button.setCallbackData("Мультфильмы");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Норвегия");
        button.setCallbackData("Норвегия");
        row.add(button);

        rows.add(row);
        row = new ArrayList<>();

        button = new InlineKeyboardButton();
        button.setText("Короткометражные фильм");
        button.setCallbackData("Короткометражные фильм");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("США");
        button.setCallbackData("США");
        row.add(button);
        button = new InlineKeyboardButton();
        button.setText("Польша");
        button.setCallbackData("Польша");
        return getInlineKeyboardMarkup(inlineKeyboardMarkup, rows, row, button);
    }
}





