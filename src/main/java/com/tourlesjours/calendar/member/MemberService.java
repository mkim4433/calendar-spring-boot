package com.tourlesjours.calendar.member;

import com.tourlesjours.calendar.member.jpa.MemberEntity;
import com.tourlesjours.calendar.member.jpa.MemberRepository;
import com.tourlesjours.calendar.member.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class MemberService {

    public final static int USER_ID_ALREADY_EXIST = 0;
    public final static int USER_SIGNUP_SUCCESS = 1;
    public final static int USER_SIGNUP_FAIL = -1;

    public final static int MODIFY_SUCCESS = 1;
    public final static int MODIFY_FAIL = 0;

    public final static int PASSWORD_CHANGE_SUCCESS = 1;
    public final static int PASSWORD_CHANGE_FAIL = 0;

    // JDBC 템플릿 사용
    private final MemberDao memberDao;
    // MyBatis 매퍼 사용
    private final MemberMapper memberMapper;
    // JPA ORM 사용
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;      // 암호화
    private final JavaMailSender javaMailSender;

    public MemberService(MemberDao memberDao,
                         PasswordEncoder passwordEncoder,
                         JavaMailSender javaMailSender,
                         MemberMapper memberMapper,
                         MemberRepository memberRepository) {
        this.memberDao = memberDao;
        this.passwordEncoder = passwordEncoder;
        this.javaMailSender = javaMailSender;
        this.memberMapper = memberMapper;
        this.memberRepository = memberRepository;
    }

    @Value("${MAIL_SENDER_ADDRESS}")
    private String mailSenderAddress;

    @Value("${MAIL_RECEIVER_ADDRESS}")
    private String mailReceiverAddress;

    // 회원가입
    public int signupConfirm(MemberDto memberDto) {

        // 기존 회원인지 확인
//        boolean isMember = memberMapper.isMember(memberDto.getId());
        boolean isMember = memberRepository.existsByMemId(memberDto.getId());

        // 회원 여부에 따라 처리
        if (!isMember) {

            // 비밀번호 암호화
            String encodedPw = passwordEncoder.encode(memberDto.getPw());
            memberDto.setPw(encodedPw);

//            int result = memberMapper.insertMember(memberDto);
//
//            if(result > 0) {
//                return USER_SIGNUP_SUCCESS;
//            } else {
//                return USER_SIGNUP_FAIL;
//            }

            MemberEntity savedMemberEntity = memberRepository.save(memberDto.toEntity());

            if (savedMemberEntity != null)
                return USER_SIGNUP_SUCCESS;
            else
                return USER_SIGNUP_FAIL;

        } else {
            return USER_ID_ALREADY_EXIST;
        }
    }

    // 회원 로그인
    public String signinConfirm(MemberDto memberDto) {

//        MemberDto selectedDto = memberMapper.selectMemberById(memberDto.getId());
//
//        if (selectedDto != null && passwordEncoder.matches(memberDto.getPw(), selectedDto.getPw())) {
//            return selectedDto.getId();
//
//        } else {
//            return null;
//        }

        Optional<MemberEntity> optionalMember = memberRepository.findByMemId(memberDto.getId());

        if (optionalMember.isPresent() &&
                passwordEncoder.matches(memberDto.getPw(), optionalMember.get().getMemPw())) {
            log.info("MEMBER LOGIN SUCCESS!!");
            return optionalMember.get().getMemId();

        } else {
            log.info("MEMBER LOGIN FAIL!!");
            return null;
        }
    }

    // 회원 정보 수정 1 (회원 정보 확인)
    public MemberDto modify(String loginedId) {

//        MemberDto dto = memberMapper.selectMemberById(loginedId);
//
//        return dto;

        Optional<MemberEntity> optionalMember = memberRepository.findByMemId(loginedId);

        if (optionalMember.isPresent()) {

            MemberEntity foundMemberEntity = optionalMember.get();

            return foundMemberEntity.toDto();
        }

        return null;
    }


    // 회원 정보 수정 2 (실제 정보 수정)
    public int modifyConfirm(MemberDto memberDto) {

//        int result = -1;
//
//        String encodedPw = passwordEncoder.encode(memberDto.getPw());
//        memberDto.setPw(encodedPw);
//
//        try {
//            result = memberMapper.updateMember(memberDto);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return result;

        String encodedPw = passwordEncoder.encode(memberDto.getPw());
        memberDto.setPw(encodedPw);

        Optional<MemberEntity> optionalMember = memberRepository.findById(memberDto.getNo());
        if (optionalMember.isPresent()) {
            MemberEntity foundMemberEntity = optionalMember.get();
            foundMemberEntity.setMemPw(memberDto.getPw());
            foundMemberEntity.setMemMail(memberDto.getMail());
            foundMemberEntity.setMemPhone(memberDto.getPhone());
            foundMemberEntity.setMemModDate(LocalDateTime.now());

            memberRepository.save(foundMemberEntity);
            return MODIFY_SUCCESS;
        }

        return MODIFY_FAIL;
    }

    // 비밀번호 찾기
    public int findPasswordConfirm(MemberDto memberDto) {

//        MemberDto selectedMemberDto = memberMapper.selectMemberByIdAndMail(memberDto);
//
//        int result = 0;
//
//        if (selectedMemberDto != null) {
//            // 새 비밀번호 생성
//            String newPw = createNewPassword();
//
//            // DB 업데이트
//            result = memberMapper.updatePassword(memberDto.getId(), passwordEncoder.encode(newPw));
//
//            if (result > 0) {
//                // 새 비밀번호 메일 발송
//                sendNewPasswordByMail(memberDto.getMail(), newPw);
//            }
//        }
//
//        return result;

       Optional<MemberEntity> optionalMember =
               memberRepository.findByMemIdAndMemMail(memberDto.getId(), memberDto.getMail());

       if (optionalMember.isPresent()) {

           MemberEntity foundMemberEntity = optionalMember.get();

           // 새 비밀번호 생성
           String newPw = createNewPassword();

           // DB 업데이트
           foundMemberEntity.setMemPw(passwordEncoder.encode(newPw));
           foundMemberEntity.setMemModDate(LocalDateTime.now());
           MemberEntity updatedMember = memberRepository.save(foundMemberEntity);

           if (updatedMember != null) {
               // 새 비밀번호 메일 발송
               sendNewPasswordByMail(memberDto.getMail(), newPw);
           }

           return PASSWORD_CHANGE_SUCCESS;
       }

       return PASSWORD_CHANGE_FAIL;
    }

    // 새 비밀번호 생성
    private String createNewPassword() {

        char[] chars = new char[]{
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

    // 새 비밀번호 메일로 전송
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
