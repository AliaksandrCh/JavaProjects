
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../WEB-INF/jspf/header.jspf"%>
<jsp:useBean id="bookList" class="com.javaee.web.beans.BookList" scope="page"/>
<jsp:useBean id="book" class="com.javaee.web.beans.Book" scope="page"/>

        <iframe src = "<%=request.getContextPath()%>/ShowPDF?index=<%=request.getParameter("index")%>" width="1024" height="600" alt="Попробуйте в другом браузере"></iframe>

<%@include file="../WEB-INF/jspf/footer.jspf"%>
