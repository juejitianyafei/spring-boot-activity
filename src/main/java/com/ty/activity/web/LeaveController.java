package com.ty.activity.web;

import com.ty.activity.common.session.SessionManage;
import com.ty.activity.domain.oa.Leave;
import com.ty.activity.domain.oa.LeaveApproval;
import com.ty.activity.service.oa.impl.LeaveWorkflowService;
import com.ty.activity.util.Page;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/leave")
public class LeaveController {

    @Autowired
    private LeaveWorkflowService leaveWorkflowService;

    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
    }

    @RequestMapping(value = "/toReadyList", method = RequestMethod.GET)
    public ModelAndView toReadyList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("leave/readyList");
        return modelAndView;
    }

    @RequestMapping(value = "/readyList", method = RequestMethod.GET)
    @ResponseBody
    public Page<Leave> readyList(Integer page, Integer rows) {
        User user = SessionManage.getCurrentUser();
        Page<Leave> pageInfo = new Page(page, rows);
        leaveWorkflowService.findTodoTasks(user.getId(), pageInfo);
        return pageInfo;
    }

    @RequestMapping(value = "/toApply", method = RequestMethod.GET)
    public ModelAndView toAdd() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("leave/apply");
        return modelAndView;
    }

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    @ResponseBody
    public String applyLeave(Leave leave) {

        User user = SessionManage.getCurrentUser();
        leave.setUserId(user.getId());
        leave.setUserName(user.getFirstName());
        leave.setApplyTime(new Date());
        Map<String, Object> variables = new HashMap<String, Object>();
        leaveWorkflowService.startWorkflow(leave, variables);
        return "success";
    }


    /**
     * 签收任务
     */
    @RequestMapping(value = "/claim")
    @ResponseBody
    public String claim(String taskId) {
        String userId = SessionManage.getCurrentUser().getId();
        leaveWorkflowService.claim(taskId, userId);
        return "success";
    }

    @RequestMapping(value = "/toViewComplete", method = RequestMethod.GET)
    public ModelAndView toViewComplete(Long id, String taskId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("leave/complete");
        Leave leave = leaveWorkflowService.getLeave(id, taskId);
        modelAndView.addObject("leave", leave);
        return modelAndView;
    }

    /**
     * 处理任务
     *
     * @param taskId
     * @param leaveApproval
     * @return
     */
    @RequestMapping(value = "/complete", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String complete(String taskId, LeaveApproval leaveApproval) {
        String assignee = SessionManage.getCurrentUser().getFirstName();
        leaveApproval.setAssignee(assignee);
        leaveWorkflowService.complete(taskId, leaveApproval);
        return "success";
    }

    /**
     * 历史任务列表
     *
     * @return
     */
    @RequestMapping(value = "/toHistoryList", method = RequestMethod.GET)
    public ModelAndView toHistoryList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("leave/historyList");
        return modelAndView;
    }

    @RequestMapping(value = "/historyList", method = RequestMethod.GET)
    @ResponseBody
    public Page<Leave> historyList(Integer page, Integer rows) {
        User user = SessionManage.getCurrentUser();
        Page<Leave> pageInfo = new Page(page, rows);
        leaveWorkflowService.findHisList(user.getId(), pageInfo);
        return pageInfo;
    }

    /**
     * 历史审批意见
     *
     * @param leaveId
     * @return
     */
    @RequestMapping(value = "/toViewAuditHis", method = RequestMethod.GET)
    public ModelAndView toViewAuditHis(String leaveId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("leave/auditHis");
        List<LeaveApproval> auditHisList = leaveWorkflowService.getAuditHis(leaveId);
        modelAndView.addObject("auditHisList", auditHisList);
        return modelAndView;
    }

}
