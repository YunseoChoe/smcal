<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="org.zerock.smcal.vo.CalendarVO" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>달력 작성하기 페이지</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            border: 1px solid #000;
            padding: 8px;
            text-align: center;
        }
    </style>
</head>
<body>
<h1>달력 작성하기 페이지</h1>

<!-- 일정 등록 폼 -->
<form action="/register" method="post" class="register-form">
    <label for="cal_date">날짜:</label>
    <input type="date" id="cal_date" name="cal_date" required>
    <label for="cal_content">내용:</label>
    <input type="text" id="cal_content" name="cal_content" required>
    <input type="submit" value="작성">
</form>


</body>
</html>
