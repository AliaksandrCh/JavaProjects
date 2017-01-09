package com.javaee.web.beans;

import com.javaee.db.DbConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AuthorList {
    List<Author> authorList = new ArrayList<>();

    private List<Author> getAuthors() {

        Statement statement = null;
        ResultSet rs= null;
        Connection connection = null;
        try {
            connection = DbConnection.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM library.author ORDER BY fio;");
            while (rs.next()) {
                Author author = new Author();
                author.setId(rs.getInt(1));
                author.setFio(rs.getString(2));
                authorList.add(author);
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
        return authorList;
    }

    public  List<Author> getAuthorList() {
        if (!authorList.isEmpty()){
            return authorList;
        } else {
            return getAuthors();
        }
    }

}