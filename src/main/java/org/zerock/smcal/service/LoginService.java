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
}
