<%--
  Created by IntelliJ IDEA.
  User: choeyunseo
  Date: 2024. 11. 6.
  Time: 오후 4:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>login</title>
</head>
<body>
    <h1>로그인 페이지</h1>
    <form method="post" action="/smcal" class="login-form">
        <input type="text" name="username" placeholder="아이디" required>
        <input type="password" name="password" placeholder="비밀번호" required>
        <input type="submit" value="로그인">
        <button onclick="window.location.href='/signup';">회원가입</button>
    </form>
</body>
</html>
