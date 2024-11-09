package org.zerock.smcal.service;

import org.zerock.smcal.dao.CalendarDAO;
import org.zerock.smcal.vo.CalendarVO;

import java.util.List;

public enum CalendarService {
    INSTANCE;

    private CalendarDAO calendarDAO;

    CalendarService() {
        calendarDAO = new CalendarDAO(); // DAO 객체 생성
    }

    // 일정 등록 메서드
    public int registerCalendar(CalendarVO calendar) throws Exception {
        return calendarDAO.calendarInsert(calendar);
    }

    // 일정 수정 메서드
    public void updateCalendar(CalendarVO calendarVO) throws Exception {
        calendarDAO.calendarUpdate(calendarVO);
    }

    // 일정 조회 메서드
    public List<CalendarVO> getAllCalendars() throws Exception {
        return calendarDAO.calendarRead();
    }

    // 일정 ID로 조회 메서드 추가
    public  CalendarVO getCalendarById(int calId) throws Exception {

        System.out.println("서비스");
        System.out.println(calendarDAO.calendarReadById(calId).getCal_content());

        return calendarDAO.calendarReadById(calId);
    }
}
