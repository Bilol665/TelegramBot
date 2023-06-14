package org.Videos.repository.movie;

public interface MovieRepository {
    String insert = """
            insert into movie(id,title,posterUrl,originalTitle,slogan,imdbRating,kpRating,year,countries,genres,plot,actors,directors,screenwriters,producers,ageLimit,isSerial)
            values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);""";
    String getMovieFromId = "select * from movie where id = ?;";
    String getMovieByName = "select * from movie where upper(title) ilike upper('%' || ? || '%');";
    String getAll = "select * from movie;";
    String update = "update movie set url = ? where id = ?;";
    String getMovieByGenre = """
            select * from movie where upper(genres) like ('%' || upper(?) || '%');""";
}
