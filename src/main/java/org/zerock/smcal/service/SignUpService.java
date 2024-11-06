package org.zerock.smcal.service;

import org.modelmapper.ModelMapper;
import org.zerock.smcal.dao.MemberDAO;
import org.zerock.smcal.dto.SignUpDTO;
import org.zerock.smcal.vo.MemberVO;

public enum SignUpService {
    INSTANCE;

    private MemberDAO memberDAO;
    private ModelMapper modelMapper;

    SignUpService() {
        this.memberDAO = new MemberDAO();
        this.modelMapper = new ModelMapper();
    }

    // 사용자 값을 데이터베이스에 저장
    public void signUp(SignUpDTO signupDTO) throws Exception {
        MemberVO member = modelMapper.map(signupDTO, MemberVO.class);
        memberDAO.insert(member);
    }

    // 중복 여부 체크
    public boolean signUpCheck(SignUpDTO signupDTO) throws Exception {
        MemberVO member = modelMapper.map(signupDTO, MemberVO.class);
        return memberDAO.duplicate(member); // true면 중복
    }
}
