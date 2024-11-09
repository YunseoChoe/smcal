package org.zerock.smcal.service;

import org.modelmapper.ModelMapper;
import org.zerock.smcal.dao.MemberDAO;
import org.zerock.smcal.vo.LoginVO;
import org.zerock.smcal.dto.LoginDTO;

public enum LoginService {
    INSTANCE;

    private MemberDAO memberDAO;
    private ModelMapper modelMapper;

    LoginService() {
        this.memberDAO = new MemberDAO();
        this.modelMapper = new ModelMapper();
    }

    public boolean login(LoginDTO loginDTO) throws Exception {
        LoginVO loginVO = this.modelMapper.map(loginDTO, LoginVO.class);
        return memberDAO.equal(loginVO);
    }

    // username으로 user_id 조회
    public int getUserIdByUsername(String username) throws Exception {
        return memberDAO.getUserId(username); // memberDAO에서 user_id를 조회
    }

    public String getUsernameById(int userId) throws Exception {
        return memberDAO.getUsername(userId);
    }
}
