package com.ty.activity.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * @author wangfei
 * @date 2019/7/13 18:21
 */
@Controller
@RequestMapping("/deployManage")
public class DeployManageController {

    //持久化服务
    @Autowired
    private RepositoryService repositoryService;

    @RequestMapping(value = "/toDeploy", method = RequestMethod.GET)
    public ModelAndView toList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("deploymanage/deploy");
        return modelAndView;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Object list() {
        return null;
    }

    @RequestMapping(value = "/toDefinationList", method = RequestMethod.GET)
    public ModelAndView toDefinationList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("deploymanage/definationList");
        return modelAndView;
    }

    /**
     * 查询流程定义列表
     *
     * @return
     */
    @RequestMapping(value = "/definationList", method = RequestMethod.GET)
    @ResponseBody
    public String definationList() {
        //ProcessDefinition对象json序列化有问题 特殊处理
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()//创建流程定义查询
                .orderByProcessDefinitionVersion().asc()//
                .list();
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().add("identityLinks");
        return JSON.toJSONString(list, filter);
    }

    /**
     * 查看流程图
     *
     * @param deploymentId 部署对象ID
     * @param imageName    资源图片名称
     */
    @RequestMapping(value = "/viewImage", method = RequestMethod.GET)
    public void definationList(String deploymentId
            , String imageName
            , HttpServletResponse response) throws IOException {
        //获取资源文件表（act_ge_bytearray）中资源图片输入流InputStream
        InputStream in = repositoryService.getResourceAsStream(deploymentId, imageName);
        //从response对象获取输出流
        OutputStream out = response.getOutputStream();
        //将输入流中的数据读取出来，写到输出流中
        for (int b = -1; (b = in.read()) != -1; ) {
            out.write(b);
        }
        out.close();
        in.close();
    }

    @RequestMapping(value = "/toDeployList", method = RequestMethod.GET)
    public ModelAndView toDeployList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("deploymanage/deployList");
        return modelAndView;
    }

    /**
     * 查询流程部署列表
     *
     * @return
     */
    @RequestMapping(value = "/deployList", method = RequestMethod.GET)
    @ResponseBody
    public String deployList() {
        List<Deployment> list = repositoryService.createDeploymentQuery()//创建部署对象查询
                .orderByDeploymenTime().asc()//
                .list();
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().add("resources");
        return JSON.toJSONString(list, filter);
    }

    /**
     * 删除流程部署
     *
     * @return
     */
    @RequestMapping(value = "/delDeploy", method = RequestMethod.GET)
    @ResponseBody
    public String deployList(String id) {
        repositoryService.deleteDeployment(id, true);
        return "success";
    }

    /**
     * 部署流程
     *
     * @param flowFile
     * @param flowName
     * @return
     */
    @RequestMapping(value = "/deploy", method = RequestMethod.POST)
    @ResponseBody
    public String deploy(@RequestParam(value = "flowFile") MultipartFile flowFile
            , @RequestParam(value = "flowName") String flowName) {
        try {
            ZipInputStream zipInputStream = new ZipInputStream(flowFile.getInputStream());
            repositoryService.createDeployment()//创建部署对象
                    .name(flowName)//添加部署名称
                    .addZipInputStream(zipInputStream)//
                    .deploy();//完成部署
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }
}
