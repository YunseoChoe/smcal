package org.zerock.smcal.controller.calendar;

import org.zerock.smcal.vo.CalendarVO;
import org.zerock.smcal.service.CalendarService;

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

@WebServlet("/read")
public class ReadController extends HttpServlet {
    private CalendarService calendarService = CalendarService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // 모든 일정을 가져옴
            List<CalendarVO> allCalendars = calendarService.getAllCalendars();

            // 오늘 날짜를 기준으로 달력 정보 설정
            LocalDate today = LocalDate.now();
            int currentMonth = today.getMonthValue(); // 현재 월
            int currentYear = today.getYear(); // 현재 년도
            int currentDay = today.getDayOfMonth(); // 오늘 날짜

            // 해당 월의 첫 번째 날짜와 마지막 날짜 계산
            LocalDate firstDayOfMonth = today.withDayOfMonth(1);
            LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

            // 달력에 표시할 날짜 리스트 생성 (첫 번째 날부터 마지막 날까지)
            List<LocalDate> daysInMonth = new ArrayList<>();
            for (LocalDate date = firstDayOfMonth; !date.isAfter(lastDayOfMonth); date = date.plusDays(1)) {
                daysInMonth.add(date);
            }

            // 각 날짜에 대한 일정 리스트를 Map으로 저장
            Map<LocalDate, List<CalendarVO>> calendarMap = new HashMap<>();
            for (CalendarVO calendar : allCalendars) {
                LocalDate calendarDate = calendar.getCal_date().toLocalDate(); // CalendarVO에서 Date를 LocalDate로 변환
                calendarMap.putIfAbsent(calendarDate, new ArrayList<>());
                calendarMap.get(calendarDate).add(calendar);
            }

            // 데이터들을 request에 설정
            request.setAttribute("daysInMonth", daysInMonth);
            request.setAttribute("calendarMap", calendarMap);
            request.setAttribute("currentMonth", currentMonth);
            request.setAttribute("currentYear", currentYear);

            // JSP 페이지로 포워드
            request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("일정 조회 중 에러가 발생하였습니다.");
        }
    }
}
