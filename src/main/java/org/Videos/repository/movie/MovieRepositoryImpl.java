package org.Videos.repository.movie;

import com.fasterxml.jackson.databind.JsonNode;
import org.Videos.model.movie.Movie;
import org.Videos.utils.Utils;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MovieRepositoryImpl implements MovieRepository {
    private MovieRepositoryImpl() {
    }

    private static final MovieRepositoryImpl instance = new MovieRepositoryImpl();
    private final Connection connection = Utils.connection();

    public static MovieRepositoryImpl getInstance() {
        return instance;
    }

    public boolean addMovie(JsonNode jsonNode) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insert);
            StringBuilder countries = new StringBuilder();
            StringBuilder genres = new StringBuilder();
            StringBuilder actors = new StringBuilder();
            StringBuilder directors = new StringBuilder();
            StringBuilder screenwriters = new StringBuilder();
            StringBuilder producers = new StringBuilder();
            for (int i = 0; i < jsonNode.get("pageProps").get("description").get("countries").size(); i++) {
                countries.append(jsonNode.get("pageProps").get("description").get("countries").get(i).get("title"));
                if (i != jsonNode.get("pageProps").get("description").get("countries").size()-1) countries.append(",");
            }
            for (int i = 0; i < jsonNode.get("pageProps").get("description").get("genres").size(); i++) {
                genres.append(jsonNode.get("pageProps").get("description").get("genres").get(i).get("title"));
                if (i != jsonNode.get("pageProps").get("description").get("genres").size()-1) genres.append(",");
            }
            for (int i = 0; i < jsonNode.get("pageProps").get("description").get("cast").get(0).get("members").size(); i++) {
                actors.append(jsonNode.get("pageProps").get("description").get("cast").get(0).get("members").get(i).get("originalName"));
                if (i != jsonNode.get("pageProps").get("description").get("cast").get(0).get("members").size()) actors.append(",");
            }
            for (int i = 0; i < jsonNode.get("pageProps").get("description").get("cast").get(1).get("members").size(); i++) {
                directors.append(jsonNode.get("pageProps").get("description").get("cast").get(1).get("members").get(i).get("originalName"));
                if (i != jsonNode.get("pageProps").get("description").get("cast").get(1).get("members").size()) directors.append(",");
            }
            for (int i = 0; i < jsonNode.get("pageProps").get("description").get("cast").get(2).get("members").size(); i++) {
                screenwriters.append(jsonNode.get("pageProps").get("description").get("cast").get(2).get("members").get(i).get("originalName"));
                if (i != jsonNode.get("pageProps").get("description").get("cast").get(2).get("members").size()) screenwriters.append(",");
            }
            for (int i = 0; i < jsonNode.get("pageProps").get("description").get("cast").get(3).get("members").size(); i++) {
                producers.append(jsonNode.get("pageProps").get("description").get("cast").get(3).get("members").get(i).get("originalName"));
                if (i != jsonNode.get("pageProps").get("description").get("cast").get(3).get("members").size()) producers.append(",");
            }
            preparedStatement.setInt(1, jsonNode.get("pageProps").get("description").get("id").asInt());
            preparedStatement.setString(2, jsonNode.get("pageProps").get("description").get("title").asText());
            preparedStatement.setString(3, jsonNode.get("pageProps").get("description").get("poster").asText());
            preparedStatement.setString(4, jsonNode.get("pageProps").get("description").get("originalTitle").asText());
            preparedStatement.setString(5, jsonNode.get("pageProps").get("description").get("slogan").asText());
            preparedStatement.setDouble(6, jsonNode.get("pageProps").get("description").get("imdbRating").asDouble());
            preparedStatement.setDouble(7, jsonNode.get("pageProps").get("description").get("kpRating").asDouble());
            preparedStatement.setInt(8, jsonNode.get("pageProps").get("description").get("year").asInt());
            preparedStatement.setString(9, countries.toString());
            preparedStatement.setString(10, genres.toString());
            preparedStatement.setString(11, jsonNode.get("pageProps").get("description").get("plot").asText());
            preparedStatement.setString(12, actors.toString());
            preparedStatement.setString(13, directors.toString());
            preparedStatement.setString(14, screenwriters.toString());
            preparedStatement.setString(15, producers.toString());
            preparedStatement.setInt(16, jsonNode.get("pageProps").get("description").get("ageLimit").asInt());
            preparedStatement.setBoolean(17, jsonNode.get("pageProps").get("description").get("isSerial").asBoolean());

            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public ArrayList<Movie> getByGenre(String genre) {
        return getMovies(genre, getMovieByGenre);
    }
    public ArrayList<Movie> getByName(String name) {
        return getMovies(name, getMovieByName);
    }

    @Nullable
    private ArrayList<Movie> getMovies(String name, String getMovieByName) {
        ArrayList<Movie> movies = new ArrayList<>();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(getMovieByName);
            preparedStatement.setString(1,name);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                movies.add(Movie.map(resultSet));
            }
            return movies;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Movie getById(Integer id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(getMovieFromId);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return Movie.map(resultSet);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public ArrayList<Movie> getAll() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(getAll);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Movie> movies = new ArrayList<>();
            while (resultSet.next()) {
                movies.add(Movie.map(resultSet));
            }
            return movies;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public void updateUrl(String url,Integer id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.setString(1,url);
            preparedStatement.setInt(2,id);

            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
