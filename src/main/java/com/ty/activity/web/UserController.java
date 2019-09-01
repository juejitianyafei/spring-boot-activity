package com.ty.activity.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.ty.activity.util.Page;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IdentityService identityService;

    @RequestMapping(value = "/toList", method = RequestMethod.GET)
    public ModelAndView toList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/list");
        return modelAndView;
    }

    /**
     * 用户列表
     *
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(Integer page, Integer rows) {
        Page<User> pageInfo = new Page(page, rows);
        UserQuery userQuery = identityService.createUserQuery();
        List<User> list = userQuery.orderByUserId().asc().list();
        long count = userQuery.count();
        pageInfo.setRows(list);
        pageInfo.setTotal(count);
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().add("entity");
        filter.getExcludes().add("pictureByteArrayRef");
        filter.getExcludes().add("picture");
        return JSON.toJSONString(pageInfo, filter);
    }

    @RequestMapping(value = "/toAdd", method = RequestMethod.GET)
    public ModelAndView toAdd() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/add");
        return modelAndView;
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(HttpServletRequest request) {

        User user = identityService.newUser(request.getParameter("id"));
        ServletRequestDataBinder lendRecordBinder = new ServletRequestDataBinder(user);
        lendRecordBinder.bind(request);
        identityService.saveUser(user);
        return "success";
    }

    /**
     * 配置用户分组
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/toConfigGroup", method = RequestMethod.GET)
    public ModelAndView toConfigGroup(String userId) {
        ModelAndView modelAndView = new ModelAndView();
        User user = identityService.createUserQuery().userId(userId).singleResult();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("user/configroup");
        return modelAndView;
    }

    /**
     * 用户列表
     *
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/groupList", method = RequestMethod.GET)
    @ResponseBody
    public Object groupList(Integer page, Integer rows) {
        Page<Group> pageInfo = new Page(page, rows);
        GroupQuery groupQuery = identityService.createGroupQuery();
        List<Group> list = groupQuery.orderByGroupId().asc().list();
        long count = groupQuery.count();
        pageInfo.setRows(list);
        pageInfo.setTotal(count);
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        return JSON.toJSONString(pageInfo, filter);
    }

    @PostMapping("/config")
    @ResponseBody
    public String config(@RequestParam(value = "userId", required = false) String userId,
                         @RequestParam(value = "groupIds[]", required = false) String[] groupIds) {
        List<Group> groupInDb = identityService.createGroupQuery().groupMember(userId).list();
        for (Group group : groupInDb) {
            identityService.deleteMembership(userId, group.getId());
        }
        for (String group : groupIds) {
            identityService.createMembership(userId, group);
        }
        return "success";
    }
}
