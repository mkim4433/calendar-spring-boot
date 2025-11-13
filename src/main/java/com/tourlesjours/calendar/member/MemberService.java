package com.tourlesjours.calendar.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    public final static int USER_ID_ALREADY_EXIST = 0;
    public final static int USER_SIGNUP_SUCCESS = 1;
    public final static int USER_SIGNUP_FAIL = -1;

    @Autowired
    MemberDao memberDao;

    public int signupConfirm(MemberDto memberDto) {

        // 기존 회원인지 확인
        boolean isMember = memberDao.isMember(memberDto.getId());

        // 회원 여부에 따라 처리
        if (!isMember) {

           int result = memberDao.insertMember(memberDto);

           if(result > 0) {
               return USER_SIGNUP_SUCCESS;
           } else {
               return USER_SIGNUP_FAIL;
           }

        // 기존 회원이면
        } else {
            return USER_ID_ALREADY_EXIST;
        }
    }
}
