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

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

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

    // 회원정보 수정
    @GetMapping("/modify")
    public String modify(HttpSession session, Model model) {

        String loginedId = String.valueOf(session.getAttribute("loginedId"));
        MemberDto loginedMemberDto = memberService.modify(loginedId);

        model.addAttribute("loginedMemberDto", loginedMemberDto);

        return "member/modify_form";
    }

    // 회원정보 수정 확인
    @PostMapping("/modify_confirm")
    public String modifyConfirm(MemberDto memberDto, Model model) {

        int result = memberService.modifyConfirm(memberDto);
        model.addAttribute("result", result);

        return "member/modify_result";
    }

    // 비밀번호 찾기
    @GetMapping("/findpassword")
    public String findPassword() {

        return "member/findpassword_form";
    }

    // 비밀번호 찾기 확인
    @PostMapping("/findpassword_confirm")
    public String findPasswordConfirm(MemberDto memberDto, Model model) {

        int result = memberService.findPasswordConfirm(memberDto);
        model.addAttribute("result", result);

        return "member/findpassword_result";
    }


}
