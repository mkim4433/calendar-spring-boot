package com.tourlesjours.calendar.member;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    MemberService memberService;

    // 회원 가입 양식
    @GetMapping("/signup")
    public String signup() {

        return "member/signup_form";
    }

    // 회원 가입 확인
    @PostMapping("/signup_confirm")
    public String signupConfirm(MemberDto memberDto, Model model) {

        int result = memberService.signupConfirm(memberDto);

        model.addAttribute("result", result);

        return "member/signup_result";
    }

    // 로그인 양식
    @GetMapping("/signin")
    public String signin() {

        return "member/signin_form";
    }

    // 로그인 확인
    @PostMapping("/signin_confirm")
    public String signinConfirm(MemberDto memberDto, Model model, HttpSession session) {

        String loginedId = memberService.signinConfirm(memberDto);
        model.addAttribute("loginedId", loginedId);

        if (loginedId != null) {
            session.setAttribute("loginedId", loginedId);
            session.setMaxInactiveInterval(60 * 30);
        }

        return "member/signin_result";
    }

    // 로그아웃 확인
    @GetMapping("/signout_confirm")
    public String signoutConfirm(HttpSession session) {

        session.invalidate();

        return "redirect:/";
    }
}
