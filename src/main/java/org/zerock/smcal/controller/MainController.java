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

@WebServlet("/smcal")
public class MainController extends HttpServlet {
    private LoginService loginService = LoginService.INSTANCE;
    private CalendarService calendarService = CalendarService.INSTANCE;

//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//        // 세션 확인
//        HttpSession session = request.getSession(false);
//        System.out.println(session.getId()); // session_id값
//
//        String loginInfo = (String) session.getAttribute("logininfo");
//        System.out.println("loginInfo : " + loginInfo);
//
//        // 세션이 존재하면 메인 페이지로 이동
//        if(loginInfo != null) {
//            try {
//                // 달력 정보를 가져옴
//                List<CalendarVO> allCalendars = calendarService.getAllCalendars();
//
//                // 오늘 날짜를 기준으로 달력 정보 설정
//                LocalDate today = LocalDate.now();
//                int currentMonth = today.getMonthValue(); // 현재 월
//                int currentYear = today.getYear(); // 현재 년도
//                int currentDay = today.getDayOfMonth(); // 오늘 날짜
//
//                // 해당 월의 첫 번째 날짜와 마지막 날짜 계산
//                LocalDate firstDayOfMonth = today.withDayOfMonth(1);
//                LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());
//
//                // 달력에 표시할 날짜 리스트 생성 (첫 번째 날부터 마지막 날까지)
//                List<LocalDate> daysInMonth = new ArrayList<>();
//                for (LocalDate date = firstDayOfMonth; !date.isAfter(lastDayOfMonth); date = date.plusDays(1)) {
//                    daysInMonth.add(date);
//                }
//
//                // 각 날짜에 대한 일정 리스트를 Map으로 저장
//                Map<LocalDate, List<CalendarVO>> calendarMap = new HashMap<>();
//                for (CalendarVO calendar : allCalendars) {
//                    LocalDate calendarDate = calendar.getCal_date().toLocalDate(); // CalendarVO에서 Date를 LocalDate로 변환
//                    calendarMap.putIfAbsent(calendarDate, new ArrayList<>());
//                    calendarMap.get(calendarDate).add(calendar);
//                }
//
//                // 데이터들을 request에 설정
//                request.setAttribute("daysInMonth", daysInMonth);
//                request.setAttribute("calendarMap", calendarMap);
//                request.setAttribute("currentMonth", currentMonth);
//                request.setAttribute("currentYear", currentYear);
//
//                // 달력 정보 출력 (디버깅용)
//                for (Map.Entry<LocalDate, List<CalendarVO>> entry : calendarMap.entrySet()) {
//                    System.out.println("날짜: " + entry.getKey() + " -> 일정 수: " + entry.getValue().size());
//                }
//
//                // 달력 정보를 전달한 뒤 메인 페이지로 포워드
//                request.getRequestDispatcher("/index.jsp").forward(request, response);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println("일정 조회 중 에러가 발생하였습니다.");
////                request.getRequestDispatcher("/errorPage.jsp").forward(request, response);
//            }
//        }
//        // 세션이 존재하지 않으면 로그인 페이지로 이동
//        else {
//            System.out.println("사용자 세션이 존재하지 않습니다. 로그인 페이지로 이동하겠습니다.");
//            request.getRequestDispatcher("/WEB-INF/member/login.jsp").forward(request, response);
//        }
//    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // 세션 확인
        HttpSession session = request.getSession(false);
        String loginInfo = (String) session.getAttribute("logininfo");

        // 세션이 존재하면 메인 페이지로 이동
        if(loginInfo != null) {
            try {
                // 달력 정보를 가져옴
                List<CalendarVO> allCalendars = calendarService.getAllCalendars();

                // 오늘 날짜를 기준으로 달력 정보 설정
                LocalDate today = LocalDate.now();
                int currentMonth = today.getMonthValue(); // 현재 월
                int currentYear = today.getYear(); // 현재 년도

                // 해당 월의 첫 번째 날짜와 마지막 날짜 계산
                LocalDate firstDayOfMonth = today.withDayOfMonth(1); // 이번 달 첫째 날
                LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth()); // 이번 달 마지막 날

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

                // 데이터를 request에 설정
                request.setAttribute("daysInMonth", daysInMonth);  // 이번 달 날짜 목록
                request.setAttribute("calendarMap", calendarMap);  // 각 날짜별 일정
                request.setAttribute("currentMonth", currentMonth); // 현재 월
                request.setAttribute("currentYear", currentYear);   // 현재 년도

                // 메인 페이지로 포워드
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("일정 조회 중 에러가 발생하였습니다.");
            }
        }
        else {
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

