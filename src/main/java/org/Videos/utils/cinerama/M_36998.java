package org.Videos.utils.cinerama;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.Videos.model.movie.Movie;
import org.Videos.repository.movie.MovieRepositoryImpl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class M_36998 {
    private final static MovieRepositoryImpl movieRepository = MovieRepositoryImpl.getInstance();
    private static JsonNode request(String url) {
        URL movie;
        try {
            movie = new URL(url);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(movie);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void main() {
        for (int i = 1; i < 37000; i++) {
            try {
                JsonNode jsonNode = request("https://cinerama.uz/_next/data/V5VxbWW1ed7b3y8eK1UNT/watch/3/" + i + ".json");
                if (jsonNode == null) {
                    continue;
                }
                boolean b = movieRepository.addMovie(jsonNode);
                System.out.println(jsonNode.get("pageProps").get("description").get("title").asText());
                System.out.println(b);
            }catch (NullPointerException e) {
                System.out.println(e.getMessage());
                continue;
            }
        }

    }

    public static void main(String[] args) {
//        for(Movie movie:movieRepository.getAll()) {
//            movieRepository.updateUrl("https://cinerama.uz/watch/6/"+movie.getId(),movie.getId());
//        }
        ArrayList<Movie> драма = movieRepository.getByGenre("драма");
        System.out.println(драма.size());
        System.out.println(драма);
    }

}
