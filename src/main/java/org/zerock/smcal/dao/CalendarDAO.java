package org.zerock.smcal.dao;

import lombok.Cleanup;
import org.zerock.smcal.util.DBConnectionUtil;
import org.zerock.smcal.vo.CalendarVO;

import java.sql.*;

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
    public int calendarUpdate(CalendarVO calendar) throws Exception {
        String sql = "UPDATE smcal_calendar SET cal_content = ?, cal_date = ? WHERE cal_id = ?";

        // DB 연결과 PreparedStatement 객체 자동 관리
        @Cleanup Connection connection = DBConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // 수정하려는 일정의 cal_content와 cal_date를 설정
        preparedStatement.setString(1, calendar.getCal_content()); // 수정할 내용
        preparedStatement.setDate(2, calendar.getCal_date());  // 수정할 날짜
        preparedStatement.setInt(3, calendar.getCal_id()); // 수정할 일정의 cal_id

        // 실행 후 업데이트된 행의 수를 반환
        int rowsAffected = preparedStatement.executeUpdate();

        // 업데이트된 행이 하나 이상 있으면 수정 성공, 0이면 실패
        if (rowsAffected > 0) {
            return rowsAffected; // 성공적으로 수정된 행 수 반환
        } else {
            throw new SQLException("Calendar update failed, no rows affected.");
        }
    }
}
