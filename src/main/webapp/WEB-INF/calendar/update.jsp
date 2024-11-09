<%--
  Created by IntelliJ IDEA.
  User: choeyunseo
  Date: 2024. 11. 9.
  Time: 오전 3:05
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
  response.setContentType("text/html; charset=UTF-8");
  response.setCharacterEncoding("UTF-8");
%>
<%@ page import="org.zerock.smcal.vo.CalendarVO" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>일정 수정</title>
  <style>
    .highlight {
      color: red; /* 강조할 색상 */
      font-weight: bold; /* 강조할 때 굵게 */
    }
  </style>
</head>
<body>
<h1>일정 수정</h1>

<%
  CalendarVO calendars = (CalendarVO) request.getAttribute("calendars");
  if (calendars != null) {
    String dateStr = calendars.getCal_date().toString(); // 날짜 문자열로 변환
    String eventContent = calendars.getCal_content();
    String formattedDate = calendars.getCal_date().toString();

%>

<form action="/update" method="post">
  <input type="hidden" name="cal_id" value="<%= calendars.getCal_id() %>">
  <input type="hidden" name="date" value="<%= dateStr %>">

  <label for="content">내용:</label><br>
  <textarea id="content" name="cal_content" rows="4" cols="50"><%= eventContent %></textarea><br><br>

  <div>
        <span class="<%= dateStr.equals(formattedDate) ? "highlight" : "" %>">
            해당 날짜: <%= dateStr %>
        </span>
  </div>
  <button type="submit">수정</button>
</form>

<%
}
%>


</body>
</html>
