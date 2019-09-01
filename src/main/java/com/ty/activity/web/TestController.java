package com.ty.activity.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author wangfei
 * @date 2019/7/13 18:21
 */
@Controller
public class TestController {
    @RequestMapping(value="/test")
    public ModelAndView hello(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("test");
        modelAndView.addObject("name","TEST!");
        return modelAndView;
    }
}
