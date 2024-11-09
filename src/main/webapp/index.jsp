

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

<!-- allCalendars 값 출력하기 -->
<%--<%--%>
<%--  List<CalendarVO> allCalendars = (List<CalendarVO>) request.getAttribute("allCalendars");--%>
<%--  for (CalendarVO calendar : allCalendars) {--%>
<%--%>--%>
<%--<p>일정 ID: <%= calendar.getCal_id() %></p>--%>
<%--<p>일정 내용: <%= calendar.getCal_content() %></p>--%>
<%--<p>일정 날짜: <%= calendar.getCal_date() %></p>--%>
<%--<hr/>--%>
<%--<%--%>
<%--  }--%>
<%--%>--%>

<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.GregorianCalendar" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.sql.Date" %>
<%@ page import="org.zerock.smcal.vo.CalendarVO" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>월간 달력</title>
  <style>
    table {
      width: 100%;
      border-collapse: collapse;
    }
    th, td {
      width: 14.2857%;
      height: 100px;
      text-align: center;
      vertical-align: top;
      border: 1px solid #000;
    }
    td {
      font-size: 20px;
    }
  </style>
</head>
<body>
<h1>현재 날짜: ${currentYear}년 ${currentMonth}월</h1>

<%
  // 현재 연도와 월 정보를 바탕으로 달력 계산
  int year = (Integer) request.getAttribute("currentYear");
  int month = (Integer) request.getAttribute("currentMonth") - 1; // Calendar의 month는 0부터 시작
  Calendar cal = new GregorianCalendar(year, month, 1);

  int startDayOfWeek = cal.get(Calendar.DAY_OF_WEEK); // 시작 요일
  int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH); // 현재 월의 총 일수

  // 일정 목록 가져오기
  List<CalendarVO> allCalendars = (List<CalendarVO>) request.getAttribute("allCalendars");

  // 날짜와 내용을 저장할 Map
  Map<String, String> scheduledEvents = new HashMap<>();
  Map<String, Integer> scheduledUserIds = new HashMap<>();

  // 날짜 포맷 설정
  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

  for (CalendarVO calendar : allCalendars) {
    Date calDate = calendar.getCal_date(); // java.sql.Date 타입
    String dateStr = sdf.format(calDate); // Date를 String으로 변환
    scheduledEvents.put(dateStr, calendar.getCal_content()); // 날짜와 내용을 매핑
    scheduledUserIds.put(dateStr, calendar.getUser_id()); // 날짜와 user_id 매핑
  }
%>

<table>
  <tr>
    <th>일</th>
    <th>월</th>
    <th>화</th>
    <th>수</th>
    <th>목</th>
    <th>금</th>
    <th>토</th>
  </tr>
  <tr>
    <%
      // 빈 칸 채우기 (시작 요일 전까지)
      for (int i = 1; i < startDayOfWeek; i++) {
    %>
    <td></td>
    <%
      }

      // 날짜 출력
      for (int day = 1; day <= daysInMonth; day++) {
        String dateStr;
        if(day < 10) {
          dateStr = year + "-" + (month + 1) + "-0" + day; // 날짜 문자열 (yyyy-MM-dd)
        } else {
          dateStr = year + "-" + (month + 1) + "-" + day; // 날짜 문자열 (yyyy-MM-dd)
        }
        String eventContent = scheduledEvents.get(dateStr); // 해당 날짜의 일정 내용
        Integer userId = scheduledUserIds.get(dateStr); // 해당 날짜의 user_id
    %>
    <td style="color: <%= eventContent != null ? "red" : "black" %>;">
      <%= day %>
      <%
        // 일정이 있는 경우 링크 추가
        if (eventContent != null) {
          // cal_id를 가져오기 위해 allCalendars에서 해당 날짜의 일정을 찾습니다.
          int calId = -1; // 기본값
          for (CalendarVO calendar : allCalendars) {
            if (sdf.format(calendar.getCal_date()).equals(dateStr)) {
              calId = calendar.getCal_id();
              break;
            }
          }
      %>
      <br/>
      <a href="/update?cal_id=<%= calId %>" style="text-decoration: none; color: inherit;">
        <span style="font-size: smaller;">작성된 내용: <%= eventContent %></span>
      </a>
      <br/>
      <span style="font-size: smaller;">작성자 id: <%= userId != null ? userId : "없음" %></span>
      <%
      } else {
      %>
      <span style="font-size: smaller;">일정 없음</span>
      <%
        }
      %>
    </td>
    <%
      // 토요일마다 줄 바꿈
      if ((day + startDayOfWeek - 1) % 7 == 0) {
    %>
  </tr><tr>
  <%
      }
    }
  %>
</tr>
</table>

<button onclick="window.location.href='/register';">캘린더 작성</button>
<button onclick="window.location.href='/logout';">로그아웃</button>

</body>
</html>
