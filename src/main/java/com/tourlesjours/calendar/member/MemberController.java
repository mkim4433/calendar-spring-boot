package com.tourlesjours.calendar.member;

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

    // 가입.
    @GetMapping("/signup")
    public String signup() {

        return "member/signup_form";
    }

    @PostMapping("/signup_confirm")
    public String signupConfirm(MemberDto memberDto, Model model) {

        int result = memberService.signupConfirm(memberDto);

        model.addAttribute("result", result);

        return "member/signup_result";
    }

}
