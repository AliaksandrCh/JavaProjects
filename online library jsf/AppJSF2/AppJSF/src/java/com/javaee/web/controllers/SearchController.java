
package com.javaee.web.controllers;

import com.javaee.db.DbConnection;
import com.javaee.web.beans.Book;
import com.javaee.web.enums.SearchType;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean (eager = true)
@SessionScoped
public class SearchController implements Serializable {
    //переменные различного вида поиска
    private SearchType searchType; 
    private Map<String, SearchType> searchList = new HashMap<String, SearchType>();
    private String searchString; 
    private ArrayList<Book> currentBookList;
    ArrayList<Integer> pageList = new ArrayList<>();
    private String selectedLetter;
    Integer selectedGenreId;
    
    //переменные для постраничности
    private Integer booksOnPage = 2;
    private Integer totalBookCount;
    private ArrayList<Integer> pageNumbers = new ArrayList<>();
    private String currentSQL;
    private Integer selectedPageNumber=1;
    private String allBooks = "select book.id, book.name, book.content, book.page_count, book.isbn, genre.name, author.fio, book.publish_year, publisher.name, book.image " +
                "from book,author,publisher,genre " +
                "where author.id = book.author_id and publisher.id = book.publisher_id and genre.id = book.genre_id order by book.id";
    

    public SearchController(){
        getAllBooks();
        ResourceBundle bundle = ResourceBundle.getBundle("com.javaee.web.nls.messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        searchList.put(bundle.getString("author_name"), searchType.AUTHOR);
        searchList.put(bundle.getString("book_name"), searchType.TITLE);
    }

    //формирует лист с количеством страниц, по которому в content мы пробегаем в <ui:repeat ...
    private void pageNumbers(Integer totalBookCount,Integer booksOnPage){
        int pageCount;
        pageList.clear();
        if(totalBookCount>booksOnPage){
            if((totalBookCount % booksOnPage) == 0){
        pageCount = (totalBookCount / booksOnPage);
        } else { pageCount = (totalBookCount / booksOnPage)+1; }
        
        for(int i=1; i<=pageCount; i++)
        pageList.add(i);
    }}
    
    //считываем номер отображаемой страницы с content.xhtml и выполняем запрос в БД (для отображения книг с использованием limit)  
    public void selectPage(){
     Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
     selectedPageNumber = Integer.valueOf(params.get("page_number"));
     getBooks(currentSQL);
    }
   
    private ArrayList<Book> getBooks(String query) {
    
        Statement statement = null;
        ResultSet rs = null;
        Connection connection = null;
        currentSQL = query;
            try {
            connection = DbConnection.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            rs.last();
            totalBookCount = rs.getRow();
            pageNumbers(totalBookCount,booksOnPage);
            if(totalBookCount>booksOnPage){
            rs = statement.executeQuery(query+" limit "+(booksOnPage*selectedPageNumber-booksOnPage)+","+booksOnPage); //переделать с использованием StringBuilder
            } else { 
                rs = statement.executeQuery(query);
                    }
            currentBookList = new ArrayList<>();
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
                currentBookList.add(book);

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
        return currentBookList;
    }

    public ArrayList<Book> getAllBooks() {
        
        return getBooks(allBooks);
       
    }

    public void getBookByGenre(){

        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedGenreId = Integer.valueOf(params.get("genre_id"));
        selectedLetter = "";
        selectedPageNumber = 1;
        
        getBooks("select book.id, book.name, book.content, book.page_count, book.isbn, genre.name, author.fio, book.publish_year, publisher.name, book.image " +
                "from book,author,publisher,genre " +
                "where author.id = book.author_id and publisher.id = book.publisher_id and genre.id = book.genre_id and book.genre_id = "+ selectedGenreId);
        
    }

    public void getBookByLetter(){
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedLetter = params.get("letter");
        selectedGenreId = -1;
        selectedPageNumber = 1;
        getBooks("select book.id, book.name, book.content, book.page_count, book.isbn, genre.name, author.fio, book.publish_year, publisher.name, book.image " +
                "from book,author,publisher,genre " +
                "where author.id = book.author_id and publisher.id = book.publisher_id and genre.id = book.genre_id and book.name LIKE '"+ selectedLetter + "%' order by book.name");
        System.out.println("");
    }

    public void bookBySearch(){

      String str = "";
      
      if (searchString.trim().length() == 0) {
            getAllBooks();
            return;
        }
        if (searchType == SearchType.TITLE) {

            str = "select book.id, book.name, book.content, book.page_count, book.isbn, genre.name, author.fio, book.publish_year, publisher.name, book.image " +
                    "from book,author,publisher,genre " +
                    "where author.id = book.author_id and publisher.id = book.publisher_id and genre.id = book.genre_id and book.name LIKE '%" + searchString + "%' order by book.name";

        } else if (searchType == SearchType.AUTHOR){
            str = "select book.id, book.name, book.content, book.page_count, book.isbn, genre.name, author.fio, book.publish_year, publisher.name, book.image " +
                    "from book,author,publisher,genre " +
                    "where author.id = book.author_id and publisher.id = book.publisher_id and genre.id = book.genre_id and author.fio LIKE '%" + searchString + "%' order by book.name";
        }

        getBooks(str);
        selectedLetter = "";
        selectedGenreId = -1;
        selectedPageNumber = 1;
    }
    
    
    public byte[] getImage(int id) {
        Statement statement = null;
        ResultSet rs = null;
        Connection connection = null;

        byte[] image = null;

        try {
            connection = DbConnection.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery("select image from book where id=" + id);

            while (rs.next()) {
                image = rs.getBytes("image");
            }

        } catch (SQLException ex) {
            
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                
            }
        }

        return image;
    }
    
    public byte[] getPDF(int pdf_id) {
        Statement statement = null;
        ResultSet rs = null;
        Connection connection = null;

        byte[] pdf = null;

        try {
            connection = DbConnection.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery("select content from book where id=" + pdf_id);

            while (rs.next()) {
                pdf = rs.getBytes("content");
            }

        } catch (SQLException ex) {
            
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                
            }
        }

        return pdf;
    }
    
    public Character[] getRussianLetters() {
        Character[] letters = new Character[33];
        letters[0] = 'А';
        letters[1] = 'Б';
        letters[2] = 'В';
        letters[3] = 'Г';
        letters[4] = 'Д';
        letters[5] = 'Е';
        letters[6] = 'Ё';
        letters[7] = 'Ж';
        letters[8] = 'З';
        letters[9] = 'И';
        letters[10] = 'Й';
        letters[11] = 'К';
        letters[12] = 'Л';
        letters[13] = 'М';
        letters[14] = 'Н';
        letters[15] = 'О';
        letters[16] = 'П';
        letters[17] = 'Р';
        letters[18] = 'С';
        letters[19] = 'Т';
        letters[20] = 'У';
        letters[21] = 'Ф';
        letters[22] = 'Х';
        letters[23] = 'Ц';
        letters[24] = 'Ч';
        letters[25] = 'Ш';
        letters[26] = 'Щ';
        letters[27] = 'Ъ';
        letters[28] = 'Ы';
        letters[29] = 'Ь';
        letters[30] = 'Э';
        letters[31] = 'Ю';
        letters[32] = 'Я';

        return letters;
    }
    
    // исправить списки - убрать image и контент

    
     
    
    
    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }

    public Map<String, SearchType> getSearchList() {
        return searchList;
    }
    
    public ArrayList<Book> getCurrentBookList() {
        return currentBookList;
    }

    
    
    
    public Integer getBooksOnPage() {
        return booksOnPage;
    }

    public Integer getTotalBookCount() {
        return totalBookCount;
    }

    public ArrayList<Integer> getPageList() {
        return pageList;
    }

    public Integer getSelectedPageNumber() {
        return selectedPageNumber;
    }

             
}
    
    

