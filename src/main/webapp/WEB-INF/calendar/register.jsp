<%--
  Created by IntelliJ IDEA.
  User: choeyunseo
  Date: 2024. 11. 6.
  Time: 오후 3:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>register</title>
</head>
<body>
  <h1>달력 작성하기 페이지</h1>
  <form action="/register" method="post" class="register-form">
    <input type="date" name="cal_date" required>
    <input type="text" name="cal_content" required>
    <input type="submit" value="작성">
  </form>




</body>
</html>
