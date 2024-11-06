package org.zerock.smcal.controller.member;

import org.zerock.smcal.dto.SignUpDTO;
import org.zerock.smcal.service.SignUpService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/signup")
public class SignUpController extends HttpServlet {
    private SignUpService signUpService = SignUpService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/member/signup.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 폼 데이터 받아오기
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        SignUpDTO signUpDTO = new SignUpDTO();
        signUpDTO.setUsername(username);
        signUpDTO.setPassword(password);

        try {
            // true -> 중복 발생
            if (signUpService.signUpCheck(signUpDTO)) {
                System.out.println("회원가입 과정에서 중복이 발생하였습니다.");
                request.getRequestDispatcher("WEB-INF/member/signuperror.jsp").forward(request, response);
            }
            // false -> 중복 없음, 회원가입 실행
            else {
                signUpService.signUp(signUpDTO);
                System.out.println("회원가입 성공!");

                // 회원가입 성공하면 바로 로그인 처리를 위한 세션 설정
                HttpSession session = request.getSession();
                String logininfo = username + ":" + password;
                session.setAttribute("logininfo", logininfo);
                System.out.println("logininfo: " + logininfo);

                // 회원가입 성공 후 메인페이지로 이동
                response.sendRedirect("/smcal");
            }
        }
        catch (Exception e) {
            System.out.println("회원가입 예외처리");
            e.printStackTrace();
            request.getRequestDispatcher("WEB-INF/member/signuperror.jsp").forward(request, response);
        }
    }
}
