package org.zerock.smcal.controller;

import lombok.SneakyThrows;
import org.zerock.smcal.service.CalendarService;
import org.zerock.smcal.service.LoginService;
import org.zerock.smcal.dto.LoginDTO;
import org.zerock.smcal.vo.CalendarVO;
import sun.tools.jconsole.JConsole;

import javax.servlet.http.HttpSession;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/smcal")
public class MainController extends HttpServlet {
    private LoginService loginService = LoginService.INSTANCE;
    private CalendarService calendarService = CalendarService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // 파라미터를 가져옴
        String monthParam = request.getParameter("month");
        String yearParam = request.getParameter("year");

        // 오늘 날짜를 기준으로 기본값 설정
        LocalDate today = LocalDate.now();
        int thisMonth = (monthParam != null) ? Integer.parseInt(monthParam) : today.getMonthValue();
        int thisYear = (yearParam != null) ? Integer.parseInt(yearParam) : today.getYear();

        // 세션 확인
        HttpSession session = request.getSession(false);
        String loginInfo = (session != null) ? (String) session.getAttribute("logininfo") : null;

        // 세션이 존재하면 메인 페이지로 이동
        if (loginInfo != null) {
            try {
                // 달력 정보를 가져옴
                List<CalendarVO> allCalendars = calendarService.getAllCalendars();

                // 해당 월의 첫 번째 날짜와 마지막 날짜 계산
                LocalDate firstDayOfMonth = LocalDate.of(thisYear, thisMonth, 1); // 지정한 달의 첫째 날
                LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth()); // 지정한 달의 마지막 날

                // 달력에 표시할 날짜 리스트 생성 (첫 번째 날부터 마지막 날까지)
                List<LocalDate> daysInMonth = new ArrayList<>();
                for (LocalDate date = firstDayOfMonth; !date.isAfter(lastDayOfMonth); date = date.plusDays(1)) {
                    daysInMonth.add(date);
                }

                // 각 날짜에 대한 일정 리스트를 Map으로 저장
                Map<LocalDate, List<CalendarVO>> calendarMap = new HashMap<>();
                for (CalendarVO calendar : allCalendars) {
                    LocalDate calendarDate = calendar.getCal_date().toLocalDate(); // CalendarVO에서 Date를 LocalDate로 변환
                    if (!calendarDate.isBefore(firstDayOfMonth) && !calendarDate.isAfter(lastDayOfMonth)) {
                        calendarMap.putIfAbsent(calendarDate, new ArrayList<>());
                        calendarMap.get(calendarDate).add(calendar);
                    }
                }

                // 달력 정보를 request에 설정
                request.setAttribute("allCalendars", allCalendars);
                request.setAttribute("daysInMonth", daysInMonth);  // 이번 달 날짜 목록
                request.setAttribute("calendarMap", calendarMap);  // 각 날짜별 일정
                request.setAttribute("currentMonth", thisMonth); // 현재 월
                request.setAttribute("currentYear", thisYear);   // 현재 년도

                // 모든 user_id를 request에 추가 (int 타입)
                List<Integer> userIds = allCalendars.stream()
                        .map(CalendarVO::getUser_id)
                        .collect(Collectors.toList());
                request.setAttribute("userIds", userIds); // 모든 user_id 리스트

                // 메인 페이지로 포워드
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "일정 조회 중 에러가 발생하였습니다."); // 에러 메시지 설정
                request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request, response); // 에러 페이지로 포워드
            }
        } else {
            // 로그인 페이지로 포워드
            request.getRequestDispatcher("/WEB-INF/member/login.jsp").forward(request, response);
        }
    }



    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException,ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String str = username + ":" + password;

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(username);
        loginDTO.setPassword(password);

        // 로그인 인증
        if(loginService.login(loginDTO)) {
            // 로그인 세션 정보 저장
            HttpSession session = request.getSession(); // 세션 가져오기
            session.setAttribute("logininfo", str); // 세션에 데이터 저장

            System.out.println("로그인 성공!");
            response.sendRedirect("/smcal"); // 로그인 성공 후 메인페이지로 이동
        }
        else {
            System.out.println("로그인에 실패하였습니다.");
            request.getRequestDispatcher("/WEB-INF/member/loginerror.jsp").forward(request, response);
        }
    }
}

