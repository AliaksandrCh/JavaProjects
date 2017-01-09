package com.javaee.web.beans;

import com.javaee.db.DbConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GenreList {

    List<Genre> genreList = new ArrayList<>();

    private List<Genre> getGenre() {

        Statement statement = null;
        ResultSet rs= null;
        Connection connection = null;
        try {
            connection = DbConnection.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM library.genre ;");
            while (rs.next()) {
                Genre genre = new Genre();
                genre.setId(rs.getInt(1));
                genre.setGenre(rs.getString(2));
                genreList.add(genre);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(statement!=null) try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if(statement!=null) try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if(statement!=null) try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return genreList;
    }

    public  List<Genre> getGenreList() {
        if (!genreList.isEmpty()){
            return genreList;
        } else {
            return getGenre();
        }
    }

}
