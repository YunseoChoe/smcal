package org.zerock.smcal.controller.calendar;

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

@WebServlet("/update")
public class UpdateController extends HttpServlet {
    private CalendarService calendarService = CalendarService.INSTANCE;
    private LoginService loginService = LoginService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 세션 정보 가져오기
        HttpSession session = request.getSession();
        String loginInfo = (String) session.getAttribute("logininfo");

        String[] loginDetails = loginInfo.split(":");
        String username = loginDetails[0];

        int calId = Integer.parseInt(request.getParameter("cal_id"));

        try {
            CalendarVO calendar = calendarService.getCalendarById(calId);
            request.setAttribute("calendars", calendar);
            int user_id = calendar.getUser_id();
            String getUsername = loginService.getUsernameById(user_id);
            if (!getUsername.equals(username)) {
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/calendar/updateerror.jsp");
                rd.forward(request, response);
            }

            System.out.println("컨트롤러");
            System.out.println(calendar);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/calendar/update.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int cal_id = Integer.parseInt(request.getParameter("cal_id"));


        System.out.println("cal_id 출력");
        System.out.println(cal_id);

        // 싱글턴 인스턴스를 사용
        CalendarService calendarService = CalendarService.INSTANCE;

        try {
            // CalendarVO 객체 생성
            CalendarVO calendar = new CalendarVO();
            calendar.setCal_id(cal_id); // calId 설정
            calendar.setCal_content(request.getParameter("cal_content"));



            // 일정 업데이트 메서드 호출
            calendarService.updateCalendar(calendar);
            response.sendRedirect("/smcal"); // 수정 후 달력 페이지로 리다이렉트
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 일정 ID입니다."); // 잘못된 ID 처리
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 서버 오류 처리
        }
    }



}
