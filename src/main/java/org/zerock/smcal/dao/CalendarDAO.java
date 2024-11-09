package org.zerock.smcal.dao;

import lombok.Cleanup;
import org.zerock.smcal.util.DBConnectionUtil;
import org.zerock.smcal.vo.CalendarVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CalendarDAO {
    // calendar insert
    public int calendarInsert(CalendarVO calendar) throws Exception {
        String sql = "INSERT INTO smcal_calendar (user_id, cal_content, cal_date) VALUES (?,?,?)";

        // DB 연결과 PreparedStatement 객체 자동 관리
        @Cleanup Connection connection = DBConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        // 값 설정
        preparedStatement.setInt(1, calendar.getUser_id());
        preparedStatement.setString(2, calendar.getCal_content());
        preparedStatement.setDate(3, calendar.getCal_date());

        // 쿼리 실행
        int affectedRows = preparedStatement.executeUpdate();

        // 생성된 키를 반환받고 ID를 반환
        if (affectedRows > 0) {
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1); // 생성된 cal_id 반환
            } else {
                throw new SQLException("Creating calendar failed, no ID obtained.");
            }
        } else {
            throw new SQLException("Creating calendar failed, no rows affected.");
        }
    }

    // calendar update
    public void calendarUpdate(CalendarVO calendar) throws Exception {
        String sql = "UPDATE smcal_calendar SET cal_content = ? WHERE cal_id = ?";

        // DB 연결과 PreparedStatement 객체 자동 관리
        @Cleanup Connection connection = DBConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // 수정하려는 일정의 cal_content와 cal_date를 설정
        preparedStatement.setString(1, calendar.getCal_content()); // 수정할 내용
        preparedStatement.setInt(2, calendar.getCal_id()); // 수정할 일정의 cal_id

        // 실행 후 업데이트된 행의 수를 반환
        int rowsAffected = preparedStatement.executeUpdate();

        // 업데이트된 행이 하나 이상 있으면 수정 성공, 0이면 실패
        if (rowsAffected < 0) {
            throw new SQLException("Calendar update failed, no rows affected.");
        }
    }

    // calendar read
    public List<CalendarVO> calendarRead() throws Exception {
        List<CalendarVO> calendarList = new ArrayList<>();

        String sql = "SELECT * FROM smcal_calendar";

        @Cleanup Connection connection = DBConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            CalendarVO calendar = new CalendarVO();
            calendar.setCal_id(resultSet.getInt("cal_id"));
            calendar.setUser_id(resultSet.getInt("user_id"));
            calendar.setCal_content(resultSet.getString("cal_content"));
            calendar.setCal_date(resultSet.getDate("cal_date"));
            calendarList.add(calendar);
        }

        return calendarList;
    }

    // calendar read by ID
    public CalendarVO calendarReadById(int calId) throws SQLException {
        CalendarVO calendar = null;
        String sql = "SELECT * FROM smcal_calendar WHERE cal_id = ?";

        try (Connection connection = DBConnectionUtil.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // cal_id를 설정
            preparedStatement.setInt(1, calId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // 결과가 존재하면 CalendarVO 객체를 생성
                if (resultSet.next()) {
                    calendar = new CalendarVO();
                    calendar.setCal_id(resultSet.getInt("cal_id"));
                    calendar.setUser_id(resultSet.getInt("user_id"));
                    calendar.setCal_content(resultSet.getString("cal_content"));
                    calendar.setCal_date(resultSet.getDate("cal_date"));
                }
            }
        } catch (SQLException e) {
            // 예외 처리 로직 추가 (로깅 등)
            throw new SQLException("Error reading calendar by ID", e);
        }

        return calendar; // 조회된 CalendarVO 객체 반환
    }

}
