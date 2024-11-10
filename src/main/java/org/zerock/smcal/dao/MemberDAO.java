package org.zerock.smcal.dao;

//import jdk.internal.org.jline.terminal.TerminalBuilder;
import lombok.Cleanup;
import org.zerock.smcal.vo.LoginVO;
import org.zerock.smcal.vo.MemberVO;
import org.zerock.smcal.util.DBConnectionUtil;


import java.lang.reflect.Member;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberDAO {
    // insert
    public void insert(MemberVO member) throws Exception {
        String sql = "INSERT INTO smcal_user (username, password) VALUES (?, ?)";

        @Cleanup Connection connection = DBConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, member.getUsername());
        preparedStatement.setString(2, member.getPassword());

        preparedStatement.execute();
    }

    // equal
    public boolean equal(LoginVO loginVO) throws Exception {
        String sql = "SELECT password FROM smcal_user WHERE username = ?";
        boolean isLoginSuccesful = false;

        @Cleanup Connection connection = DBConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        try(PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, loginVO.getUsername());

            try(ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String dbPassword = rs.getString("password");
                    if (dbPassword.equals(loginVO.getPassword())) {
                        isLoginSuccesful = true;
                    }
                }
            }
        }
        return isLoginSuccesful;
    }

    // duplicate check
    public boolean duplicate(MemberVO member) throws SQLException {
        String sql = "SELECT username FROM smcal_user WHERE username = ?";
        boolean isDuplicated = false; // 중복인지 값을 저장하는 변수

        @Cleanup Connection connection = DBConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        try(PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, member.getUsername());

            ResultSet rs = pstmt.executeQuery();
//            System.out.println((rs.next())); // 확인용 출력

            return rs.next(); // true이면 중복
        }
    }

    // username으로 user_id 조회
    public int getUserId(String username) throws SQLException {
        String sql = "SELECT user_id FROM smcal_user WHERE username = ?";

        // DB 연결과 PreparedStatement 객체 자동 관리
        @Cleanup Connection connection = DBConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, username); // username 설정

        // 쿼리 실행 후 결과 받아오기
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt("user_id"); // user_id를 반환
        } else {
            throw new SQLException("No user found with username: " + username);
        }
    }

    public String getUsername(int user_id) throws SQLException {
        String sql = "SELECT username FROM smcal_user WHERE user_id = ?";

        // DB 연결과 PreparedStatement 객체 자동 관리
        @Cleanup Connection connection = DBConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setInt(1, user_id); // username 설정

        // 쿼리 실행 후 결과 받아오기
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getString("username"); // user_id를 반환
        } else {
            throw new SQLException("No user found with user_id: " + user_id);
        }
    }
}
