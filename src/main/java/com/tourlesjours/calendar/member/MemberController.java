package com.tourlesjours.calendar.member;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원 가입 양식
    @GetMapping("/signup")
    public String signup() {
        log.info("signup()");

        return "member/signup_form";
    }

    // 회원 가입 확인
    @PostMapping("/signup_confirm")
    public String signupConfirm(MemberDto memberDto, Model model) {
        log.info("signupConfirm()");

        int result = memberService.signupConfirm(memberDto);

        model.addAttribute("result", result);

        return "member/signup_result";
    }

    // 로그인 양식
    @GetMapping("/signin")
    public String signin() {
        log.info("signin()");

        return "member/signin_form";
    }

    // 회원정보 수정
    @GetMapping("/modify")
    public String modify(HttpSession session, Model model, Principal principal) {
        log.info("modify()");

        MemberDto loginedMemberDto = memberService.modify(principal.getName());
        model.addAttribute("loginedMemberDto", loginedMemberDto);

        return "member/modify_form";
    }

    // 회원정보 수정 확인
    @PostMapping("/modify_confirm")
    public String modifyConfirm(MemberDto memberDto, Model model) {
        log.info("modifyConfirm()");

        int result = memberService.modifyConfirm(memberDto);
        model.addAttribute("result", result);

        return "member/modify_result";
    }

    // 비밀번호 찾기
    @GetMapping("/findpassword")
    public String findPassword() {
        log.info("findPassword()");

        return "member/findpassword_form";
    }

    // 비밀번호 찾기 확인
    @PostMapping("/findpassword_confirm")
    public String findPasswordConfirm(MemberDto memberDto, Model model) {
        log.info("findPasswordConfirm()");

        int result = memberService.findPasswordConfirm(memberDto);
        model.addAttribute("result", result);

        return "member/findpassword_result";
    }

    // 로그인 결과
    @GetMapping("/signin_result")
    public String signinResult(@RequestParam(value = "loginedId", required = false) String loginedId, Model model) {
        log.info("signinResult()");

        model.addAttribute("loginedId", loginedId);

        return "member/signin_result";
    }

}
