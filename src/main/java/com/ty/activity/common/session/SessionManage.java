package com.ty.activity.common.session;


import org.activiti.engine.identity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class SessionManage {

    public static final String CURRENT_USER = "user";

    public static User getCurrentUser(){
        HttpServletRequest request = getCurrentRequest();
        HttpSession session = request.getSession();
        User userInfo = (User) session.getAttribute(CURRENT_USER);
        return  userInfo;
    }

    private static HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        return request;
    }

    public static void setCurrentUser(User user){
        HttpServletRequest request = getCurrentRequest();
        HttpSession session = request.getSession();
        session.setAttribute(SessionManage.CURRENT_USER,user);
    }

}
