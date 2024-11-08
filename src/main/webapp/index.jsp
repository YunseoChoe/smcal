

<%--<%@ page import="org.zerock.smcal.vo.CalendarVO" %>--%>
<%--<%@ page import="java.util.List" %>--%>
<%--<%@ page import="java.time.LocalDate" %>--%>
<%--<%@ page import="java.util.Map" %>--%>
<%--  Created by IntelliJ IDEA.--%>
<%--  User: choeyunseo--%>
<%--  Date: 2024. 11. 6.--%>
<%--  Time: 오후 3:19--%>
<%--  To change this template use File | Settings | File Templates.--%>

<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%--<html>--%>
<%--  <head>--%>
<%--    <meta charset="UTF-8">--%>
<%--    <title>main</title>--%>
<%--  </head>--%>
<%--  <body>--%>
<%--    <h1>메인 페이지</h1>--%>
<%--    <input type="date" />--%>
<%--    <br>--%>
<%--    <button onclick="window.location.href='/register';">캘린더 작성</button>--%>
<%--    <button onclick="window.location.href='/logout';">로그아웃</button>--%>
<%--  </body>--%>
<%--</html>--%>

<%@ page import="org.zerock.smcal.vo.CalendarVO" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>일정</title>
</head>
<body>
<h1>현재 날짜: ${currentYear}년 ${currentMonth}월</h1>

<!-- allCalendars 값 출력하기 -->
<%
  List<CalendarVO> allCalendars = (List<CalendarVO>) request.getAttribute("allCalendars");
  for (CalendarVO calendar : allCalendars) {
%>
<p>일정 ID: <%= calendar.getCal_id() %></p>
<p>일정 내용: <%= calendar.getCal_content() %></p>
<p>일정 날짜: <%= calendar.getCal_date() %></p>
<hr/>
<%
  }
%>
</body>
</html>

