package com.tourlesjours.calendar.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class MemberSigninInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        if (session != null) {
            Object obj = session.getAttribute("loginedId");
            if (obj != null) {
                return true;
            }
        }

        response.sendRedirect("/member/signin");
        return false;
    }
}
