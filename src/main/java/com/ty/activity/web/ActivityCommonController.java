package com.ty.activity.web;

import com.ty.activity.service.common.ActivityCommonServcie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * actiovity公共controller
 */
@Slf4j
@Controller
@RequestMapping("/activity")
public class ActivityCommonController {

    @Autowired
    private ActivityCommonServcie activityCommonServcie;

    @RequestMapping(value = "/toViewFlowImg", method = RequestMethod.GET)
    public ModelAndView toViewFlowImg(String pid) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("leave/flowimg");
        modelAndView.addObject("processInstanceId", pid);
        return modelAndView;
    }

    //查看流程图
    @RequestMapping(value = "/image", method = RequestMethod.GET)
    public void image(HttpServletResponse response,
                      @RequestParam String processInstanceId) throws IOException {
        InputStream is = activityCommonServcie.getDiagram(processInstanceId);
        if (is == null)
            return;
        response.setContentType("image/png");
        BufferedImage image = ImageIO.read(is);
        OutputStream out = response.getOutputStream();
        ImageIO.write(image, "png", out);
        is.close();
        out.close();
    }
}
