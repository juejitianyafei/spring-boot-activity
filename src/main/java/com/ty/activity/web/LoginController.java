package com.ty.activity.web;

import com.ty.activity.common.session.SessionManage;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author wangfei
 * @date 2019/7/13 18:21
 */
@Controller
@Slf4j
public class LoginController {

    @Autowired
    private IdentityService identityService;

    /**
     * 登录系统
     *
     * @param userName
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam("username") String userName,
                        @RequestParam("password") String password,
                        HttpSession session) {
        log.debug("logon request: {username={}, password={}}", userName, password);
        boolean checkPassword = identityService.checkPassword(userName, password);
        if (checkPassword) {

            // read user from database
            User user = identityService.createUserQuery().userId(userName).singleResult();
            SessionManage.setCurrentUser(user);

            List<Group> groupList = identityService.createGroupQuery().groupMember(userName).list();
            session.setAttribute("groups", groupList);

            String[] groupNames = new String[groupList.size()];
            for (int i = 0; i < groupNames.length; i++) {
                System.out.println(groupList.get(i).getName());
                groupNames[i] = groupList.get(i).getName();
            }

            session.setAttribute("groupNames", ArrayUtils.toString(groupNames));

            return "sys/main";
        } else {
            return "index";
        }
    }

    /**
     * 登录系统
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String toLogin() {
        return "index";
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "index";
    }
}
