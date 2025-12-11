package com.tourlesjours.calendar.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;

@Service
public class MemberService {

    public final static int USER_ID_ALREADY_EXIST = 0;
    public final static int USER_SIGNUP_SUCCESS = 1;
    public final static int USER_SIGNUP_FAIL = -1;

    private final MemberDao memberDao;

    private final PasswordEncoder passwordEncoder;    // 암호화

    private final JavaMailSender javaMailSender;

    public MemberService(MemberDao memberDao,
                         PasswordEncoder passwordEncoder,
                         JavaMailSender javaMailSender) {
        this.memberDao = memberDao;
        this.passwordEncoder = passwordEncoder;
        this.javaMailSender = javaMailSender;
    }

    @Value("${MAIL_SENDER_ADDRESS}")
    private String mailSenderAddress;

    @Value("${MAIL_RECEIVER_ADDRESS}")
    private String mailReceiverAddress;

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

        String encodedPw = passwordEncoder.encode(memberDto.getPw());
        memberDto.setPw(encodedPw);

        try {
            result = memberDao.updateMember(memberDto);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public int findPasswordConfirm(MemberDto memberDto) {

        MemberDto selectedMemberDto = memberDao.selectMemberByIdAndMail(memberDto);

        int result = 0;

        if (selectedMemberDto != null) {
            // 새 비밀번호 생성
            String newPw = createNewPassword();

            // DB 업데이트
            result = memberDao.updatePassword(memberDto.getId(), passwordEncoder.encode(newPw));

            if (result > 0) {
                // 새 비밀번호 메일 발송
                sendNewPasswordByMail(memberDto.getMail(), newPw);
            }
        }

        return result;
    }

    private String createNewPassword() {

        char[] chars = new char[] {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                'u', 'v', 'w', 'x', 'y', 'z'
        };

        StringBuffer stringBuffer = new StringBuffer();
        SecureRandom random = new SecureRandom();
        random.setSeed(new Date().getTime());

        int index = 0;
        int length = chars.length;
        for (int i = 0; i < 8; i++) {
            index = random.nextInt(length);

            if (index % 2 == 0)
                stringBuffer.append(String.valueOf(chars[index]).toUpperCase());
            else
                stringBuffer.append(String.valueOf(chars[index]).toLowerCase());
        }

        return stringBuffer.toString();
    }

    private void sendNewPasswordByMail(String mail, String newPw) {

        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(mail);
        message.setTo(mailReceiverAddress);
        message.setSubject("[My Calendar] 새 비밀번호 안내입니다.");
        message.setText("새 비밀번호: " + newPw);
        message.setFrom(mailSenderAddress);

        javaMailSender.send(message);
    }
}
