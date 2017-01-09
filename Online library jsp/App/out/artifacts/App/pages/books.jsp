<%@page import="com.javaee.web.beans.Book"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.javaee.enums.SearchType" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@include file="../WEB-INF/jspf/header.jspf"%>
<%@include file="../WEB-INF/jspf/left_menu.jspf"%>
<jsp:useBean id="bookList" class="com.javaee.web.beans.BookList" scope="page"/>
<%@include file="../WEB-INF/jspf/letter.jspf"%>

<%request.setCharacterEncoding("UTF-8");%>

<div class="book_list">

<%
    ArrayList<Book> list = null;

    if (request.getParameter("genre_id") != null){
        int genre = 0;
        genre = Integer.valueOf(request.getParameter("genre_id"));
        list = bookList.getBookByGenre(genre);

    } else if (request.getParameter("letter")!=null) {

        list = bookList.getBookByLetter(request.getParameter("letter"));

    } else if (!request.getParameter("search_form").trim().isEmpty()) {
        if(request.getParameter("select").equals("Название")) {
        list = bookList.getBookBySearch(request.getParameter("search_form"), SearchType.TITLE);
        } else {
        list = bookList.getBookBySearch(request.getParameter("search_form"), SearchType.AUTHOR);
        }
    }
    session.setAttribute("currentBookList",list);

    for (Book book: list){
%>

    <div class="book">
        <p><%=book.getBookName()%> </p>
        <img src="<%=request.getContextPath()%>/ShowImage?index=<%=list.indexOf(book)%>" width="190" height="250" alt="Обложка"/>
        <p>Кличество страниц: <%=book.getPageCount()%> </p>
        <p>ISBN: <%=book.getIsbn()%> </p>
        <p>Автор: <%=book.getAuthor()%> </p>
        <p><a href="content.jsp?index=<%=list.indexOf(book)%>">Читать</a> </p>



    </div>
<%}%>



<%@include file="../WEB-INF/jspf/footer.jspf"%>