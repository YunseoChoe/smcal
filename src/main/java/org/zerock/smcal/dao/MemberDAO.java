package org.zerock.smcal.dao;

import jdk.internal.org.jline.terminal.TerminalBuilder;
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
}
