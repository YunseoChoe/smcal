package org.zerock.smcal.controller.member;

import lombok.SneakyThrows;
import org.zerock.smcal.service.LoginService;
import org.zerock.smcal.dto.LoginDTO;
import sun.tools.jconsole.JConsole;

import javax.servlet.http.HttpSession;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/smcal")
public class LoginController extends HttpServlet {
    private LoginService loginService = LoginService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // 세션 확인
        HttpSession session = request.getSession(false);
        System.out.println(session.getId()); // session_id값

        String loginInfo = (String) session.getAttribute("logininfo");
        System.out.println("loginInfo : " + loginInfo);

        // 세션이 존재하면 메인 페이지로 이동
        if(loginInfo != null) {
            System.out.println("세션에 저장된 logininfo: " + loginInfo);
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
        // 세션이 존재하지 않으면 로그인 페이지로 이동
        else {
            System.out.println("사용자 세션이 존재하지 않습니다. 로그인 페이지로 이동하겠습니다.");
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
