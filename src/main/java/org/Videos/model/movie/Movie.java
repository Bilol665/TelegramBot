package org.Videos.model.movie;

import lombok.*;
import lombok.experimental.Accessors;

import java.sql.ResultSet;
import java.sql.SQLException;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class Movie {
    private Integer id;
    private String title;
    private String postUrl;
    private String originalTitle;
    private String slogan;
    private Double imdbRating;
    private Double kpRating;
    private Integer year;
    private String countries;
    private String genres;
    private String plot;
    private String actors;
    private String directors;
    private String screenwriters;
    private String producers;
    private Integer ageLimit;
    private Boolean isSerial;
    private String url;

    public static Movie map(ResultSet resultSet) {
        try {
            return new Movie()
                    .setId(resultSet.getInt("id"))
                    .setTitle(resultSet.getString("title"))
                    .setPostUrl(resultSet.getString("posterURL"))
                    .setOriginalTitle(resultSet.getString("originalTitle"))
                    .setSlogan(resultSet.getString("slogan"))
                    .setImdbRating(resultSet.getDouble("imdbRating"))
                    .setKpRating(resultSet.getDouble("kpRating"))
                    .setYear(resultSet.getInt("year"))
                    .setCountries(resultSet.getString("countries"))
                    .setGenres(resultSet.getString("genres"))
                    .setPlot(resultSet.getString("plot"))
                    .setActors(resultSet.getString("actors"))
                    .setDirectors(resultSet.getString("directors"))
                    .setScreenwriters(resultSet.getString("screenwriters"))
                    .setProducers(resultSet.getString("producers"))
                    .setAgeLimit(resultSet.getInt("ageLimit"))
                    .setIsSerial(resultSet.getBoolean("isSerial"))
                    .setUrl(resultSet.getString("url"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
