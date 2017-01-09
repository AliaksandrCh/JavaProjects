
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="utf-8">
  <title>Онлайн Библиотека</title>
  <link href="css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="main" id="1">
  <div class="header" id="2">
    <img src="images/bookstack.png">
    <p> Онлайн Библиотека </p>
  </div>
  <div class="content">
    <p> Добро пожаловать в онлайн библиотеку, где вы сможете найти любую книгу на ваш вкус. Доступны функции поиска, просмотра, сортировки и многие другие.
      <br>Проект находится в разработке, поэтому дизайн и функционал будет постоянно дорабатываться.
      <br>По всем вопросам обращайтесь по адресу <a href="mailto:support@testlibrary.com">support@testlibrary.com</a></p>
  </div>

  <p class="text">Для входа введите свои данные, в форму ниже!</p>

  <form class="login_form" name="username" action="pages/main.jsp" method="post">
    Name: <input type="text" name="username" value="" size="20">
    <input type="submit" value="Войти">
  </form>
  <div class="footer">
    <p> Разработчик Чуев Александр, 2016 г.</p>
  </div>

</div>

</body>
</html>
