package org.zerock.smcal.controller.calendar;

import org.zerock.smcal.dto.LoginDTO;
import org.zerock.smcal.service.CalendarService;
import org.zerock.smcal.service.LoginService;
import org.zerock.smcal.vo.CalendarVO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/register")
public class RegisterController extends HttpServlet {
    private CalendarService calendarService = CalendarService.INSTANCE;
    private LoginService loginService = LoginService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 일정 등록 페이지로 이동
        System.out.println("일정 등록하기로 이동 중 ..");

        try {
            // 모든 일정 목록을 가져오기
            List<CalendarVO> calendars = CalendarService.INSTANCE.getAllCalendars();

            // 일정 목록을 request에 저장
            request.setAttribute("calendars", calendars);
            System.out.println(calendars);

            // 일정 등록 페이지로 포워드
            request.getRequestDispatcher("/WEB-INF/calendar/register.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace(); // 예외 로그 출력
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "일정 목록 조회 중 오류가 발생했습니다.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // 세션 정보 가져오기
            HttpSession session = request.getSession();
            String loginInfo = (String) session.getAttribute("logininfo");

            // 로그인 정보가 없으면 로그인 페이지로 리다이렉트
            if (loginInfo == null) {
                System.out.println("로그인이 되어있지 않아 사용자를 찾을 수 없습니다. 로그인 페이지로 이동합니다..");
                response.sendRedirect("/smcal");
                return;
            }

            // 로그인 정보 파싱 (username 가져오기)
            String[] loginDetails = loginInfo.split(":");
            String username = loginDetails[0];

            // 로그인 DTO 설정
            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setUsername(username);

            // user_id를 LoginService를 통해 가져오기
            int user_id = loginService.getUserIdByUsername(username);

            // 폼 데이터 받아오기
            String cal_content = request.getParameter("cal_content");
            String cal_date_str = request.getParameter("cal_date"); // 입력된 날짜 (yyyy-MM-dd 형식의 문자열)

            System.out.println("입력받은 일정 출력: " + user_id + ", " + cal_content + ", " + cal_date_str);

            // cal_date_str을 java.sql.Date로 변환
            Date cal_date = null;
            if (cal_date_str != null && !cal_date_str.isEmpty()) {
                try {
                    cal_date = Date.valueOf(cal_date_str);
                } catch (IllegalArgumentException e) {
                    System.out.println("날짜 형식이 잘못되었습니다.");
                    response.sendRedirect("/smcal"); // 날짜 형식이 잘못되었으면 리다이렉트
                    return;
                }
            }

            // CalendarVO 객체 생성 및 값 설정
            CalendarVO calendar = new CalendarVO();
            calendar.setUser_id(user_id);
            calendar.setCal_content(cal_content);
            calendar.setCal_date(cal_date);

            // 일정 등록 처리
            System.out.println("일정 등록 중...");
            int cal_id = calendarService.registerCalendar(calendar); // 등록 함수 호출

            // 등록 성공 후 메인 페이지로 리다이렉트
            response.sendRedirect("/smcal"); // 메인 페이지로 리다이렉트

        } catch (Exception e) {
            // 예외 처리: 일정 등록 실패 시
            System.out.println("일정 등록에 실패하였습니다.");
            e.printStackTrace();
            response.sendRedirect("/smcal"); // 등록 실패시 리다이렉트
        }
    }

}
