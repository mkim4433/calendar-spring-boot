package com.tourlesjours.calendar.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    public final static int USER_ID_ALREADY_EXIST = 0;
    public final static int USER_SIGNUP_SUCCESS = 1;
    public final static int USER_SIGNUP_FAIL = -1;

    @Autowired
    MemberDao memberDao;
    @Autowired
    PasswordEncoder passwordEncoder;    // 암호화

    public int signupConfirm(MemberDto memberDto) {

        // 기존 회원인지 확인
        boolean isMember = memberDao.isMember(memberDto.getId());

        // 회원 여부에 따라 처리
        if (!isMember) {

            // 비밀번호 암호화
            String encodedPw = passwordEncoder.encode(memberDto.getPw());
            memberDto.setPw(encodedPw);

           int result = memberDao.insertMember(memberDto);

           if(result > 0) {
               return USER_SIGNUP_SUCCESS;
           } else {
               return USER_SIGNUP_FAIL;
           }

        } else {
            return USER_ID_ALREADY_EXIST;
        }
    }

    public String signinConfirm(MemberDto memberDto) {

        MemberDto selectedDto = memberDao.selectMemberById(memberDto.getId());

        if (selectedDto != null && passwordEncoder.matches(memberDto.getPw(), selectedDto.getPw())) {
            return selectedDto.getId();

        } else {
            return null;                
        }
    }

    public MemberDto modify(String loginedId) {

        MemberDto dto = memberDao.selectMemberById(loginedId);

        return dto;
    }

    public int modifyConfirm(MemberDto memberDto) {

        int result = -1;

        try {
            result = memberDao.updateMember(memberDto);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
