package com.javaee.web.beans;

import com.javaee.db.DbConnection;
import com.javaee.enums.SearchType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class BookList {

    ArrayList<Book> bookList = new ArrayList<>();

    private ArrayList<Book> getBooks(String query) {

        Statement statement = null;
        ResultSet rs = null;
        Connection connection = null;
        try {
            connection = DbConnection.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt(1));
                book.setBookName(rs.getString(2));
                book.setContent(rs.getBytes(3));
                book.setPageCount(rs.getInt(4));
                book.setIsbn(rs.getString(5));
                book.setGenre(rs.getString(6));
                book.setAuthor(rs.getString(7));
                book.setPublishYear(rs.getInt("publish_year"));
                book.setPublisher(rs.getString(9));
                book.setImage(rs.getBytes(10));
                bookList.add(book);

            }

        } catch (SQLException e) {

        } finally {
            if (statement != null) try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (statement != null) try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (statement != null) try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return bookList;
    }

    public ArrayList<Book> getAllBooks() {
        if (!bookList.isEmpty()) {
            return bookList;
        } else {
            return getBooks("SELECT * FROM library.book ORDER BY name;");
        }
    }

    public ArrayList<Book> getBookByGenre(Integer genre){

        return getBooks("select book.id, book.name, book.content, book.page_count, book.isbn, genre.name, publisher.name, author.fio, book.publish_year, book.image " +
                "from book,author,publisher,genre " +
                "where author.id = book.author_id and publisher.id = book.publisher_id and genre.id = book.genre_id and book.genre_id = "+ genre);
    }

    public ArrayList<Book> getBookByLetter(String letter){

        return getBooks("select book.id, book.name, book.content, book.page_count, book.isbn, genre.name, publisher.name, author.fio, book.publish_year, book.image " +
                "from book,author,publisher,genre " +
                "where author.id = book.author_id and publisher.id = book.publisher_id and genre.id = book.genre_id and book.name LIKE '"+ letter + "%' order by book.name");
    }

    public ArrayList<Book> getBookBySearch(String search, SearchType type){

        String str ="";
        if (type ==SearchType.TITLE) {

            str = new String("select book.id, book.name, book.content, book.page_count, book.isbn, genre.name, publisher.name, author.fio, book.publish_year, book.image " +
                    "from book,author,publisher,genre " +
                    "where author.id = book.author_id and publisher.id = book.publisher_id and genre.id = book.genre_id and book.name LIKE '%" + search + "%' order by book.name");

        } else if (type==SearchType.AUTHOR){
            str = new String("select book.id, book.name, book.content, book.page_count, book.isbn, genre.name, publisher.name, author.fio, book.publish_year, book.image " +
                    "from book,author,publisher,genre " +
                    "where author.id = book.author_id and publisher.id = book.publisher_id and genre.id = book.genre_id and author.fio LIKE '%" + search + "%' order by book.name");
        }

        return getBooks(str);
    }

//    public  getBookContent (int book_id){
//        String str = new String("select book.content from book where book.id = " + book_id);
//        return getBooks(str);
//
//    }

}
