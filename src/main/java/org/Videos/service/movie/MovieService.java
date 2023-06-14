package org.Videos.service.movie;

import org.Videos.model.movie.Movie;
import org.Videos.repository.movie.MovieRepositoryImpl;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class MovieService {
    private final static MovieService instance = new MovieService();
    private final static MovieRepositoryImpl movieRepository = MovieRepositoryImpl.getInstance();
    public static MovieService getInstance() {
        return instance;
    }

    public boolean isOnlyNumber(String string) {
        for(int i=0;i<string.length();i++){
            if(!Character.isDigit(string.charAt(i))) return false;
        }
        return true;
    }
    public boolean checkForZero(String genre) {
        return movieRepository.getByGenre(genre).size() != 0;
    }
    public InlineKeyboardMarkup searchByGenreInlineKeyboard(String genre) {
        ArrayList<Movie> movies = movieRepository.getByGenre(genre);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        int ko=0;
        InlineKeyboardButton button;
        System.out.println(movies.size());
        for (Movie movie:movies) {
            button = new InlineKeyboardButton();
            button.setText(movie.getTitle());
            button.setCallbackData(movie.getId().toString());

            row.add(button);
            if(++ko % 3 == 0) {
                rows.add(row);
                row = new ArrayList<>();
            }
        }
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    @NotNull
    private InlineKeyboardMarkup getInlineKeyboardMarkup(ArrayList<Movie> movies) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        int ko=0;
        InlineKeyboardButton button;
        System.out.println(movies.size());
        for (Movie movie:movies) {
            button = new InlineKeyboardButton();
            button.setText(movie.getTitle());
            button.setCallbackData(movie.getId().toString());

            row.add(button);
            if(++ko % 3 == 0) {
                rows.add(row);
                row = new ArrayList<>();
            }
        }
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup searchByNameInlineKeyboard(String name) {
        ArrayList<Movie> movies = movieRepository.getByName(name);
        return getInlineKeyboardMarkup(movies);
    }
    public Movie getById(Integer id) {
        return movieRepository.getById(id);
    }
}
