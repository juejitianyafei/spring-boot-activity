package com.ty.activity.service.oa.impl;

import com.ty.activity.domain.oa.Leave;
import com.ty.activity.domain.oa.LeaveApproval;
import com.ty.activity.service.oa.LeaveService;
import com.ty.activity.util.Page;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Component
@Transactional(rollbackFor = Exception.class)
public class LeaveWorkflowService {

    @Autowired
    private LeaveService leaveService;
    @Autowired
    private ProcessEngine processEngine;

    /**
     * 启动流程
     *
     * @param entity
     */
    public ProcessInstance startWorkflow(Leave entity, Map<String, Object> variables) {
        //保存记录
        leaveService.save(entity);
        log.debug("save entity: {}", entity);
        String businessKey = entity.getId().toString();

        ProcessInstance processInstance;
        try {
            // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
            processEngine.getIdentityService().
                    setAuthenticatedUserId(entity.getUserId());

            processInstance = processEngine.getRuntimeService()
                    .startProcessInstanceByKey("leave", businessKey, variables);
            String processInstanceId = processInstance.getId();
            entity.setProcessInstanceId(processInstanceId);
            log.debug("start process of {key={}, bkey={}, pid={}, variables={}}", new Object[]{"leave", businessKey, processInstanceId, variables});
        } finally {
            processEngine.getIdentityService()
                    .setAuthenticatedUserId(null);
        }
        return processInstance;
    }

    @Transactional(readOnly = true)
    public void findTodoTasks(String userId, Page page) {
        List<Leave> results = new ArrayList<Leave>();

        // 根据当前人的ID查询
        TaskQuery taskQuery = processEngine.getTaskService().
                createTaskQuery().taskCandidateOrAssigned(userId);
        List<Task> tasks = taskQuery.list();

        // 根据流程的业务ID查询实体并关联
        for (Task task : tasks) {
            String processInstanceId = task.getProcessInstanceId();
            ProcessInstance processInstance = processEngine.getRuntimeService()
                    .createProcessInstanceQuery().processInstanceId(processInstanceId)
                    .active().singleResult();
            if (processInstance == null) {
                continue;
            }
            String businessKey = processInstance.getBusinessKey();
            if (businessKey == null) {
                continue;
            }
            Leave leave = leaveService.get(new Long(businessKey));
            leave.setProcessInstanceId(processInstance.getId());
            leave.setProcessDefinitionVersion(processInstance.getProcessDefinitionVersion());
            leave.setProcessDefinitionSuspended(processInstance.isSuspended());
            leave.setTaskId(task.getId());
            leave.setAssignee(task.getAssignee());
            leave.setTaskName(task.getName());
            leave.setTaskCreateTime(task.getCreateTime());
            results.add(leave);
        }

        page.setTotal(taskQuery.count());
        page.setRows(results);

    }


    /**
     * 签收
     *
     * @param taskId
     * @param userId
     */
    public void claim(String taskId, String userId) {
        processEngine.getTaskService().claim(taskId, userId);
    }

    public void complete(String taskId, LeaveApproval leaveApproval) {
        leaveApproval.setOprateTime(new Date());
        leaveService.saveLeaveApproval(leaveApproval);
        Map<String, Object> vars = new HashMap<>();
        vars.put("approvalResult", leaveApproval.getApprovalResult());
        processEngine.getTaskService().complete(taskId, vars);
    }

    public Leave getLeave(Long id, String taskId) {
        Leave leave = leaveService.get(id);
        Task task = processEngine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
        leave.setTaskId(task.getId());
        leave.setTaskName(task.getName());
        leave.setTaskDefinitionKey(task.getTaskDefinitionKey());
        return leave;
    }

    @Transactional(readOnly = true)
    public List<Leave> findHisList(String id, Page<Leave> pageInfo) {
        List<Leave> results = new ArrayList<Leave>();
        HistoricProcessInstanceQuery query = processEngine.getHistoryService().createHistoricProcessInstanceQuery()
                .processDefinitionKey("leave").finished().orderByProcessInstanceEndTime().desc();
        List<HistoricProcessInstance> list = query.listPage(pageInfo.getStart(), pageInfo.getPagesize());
//        String processInstanceId = "";
        // 关联业务实体
        for (HistoricProcessInstance historicProcessInstance : list) {
            String businessKey = historicProcessInstance.getBusinessKey();
            Leave leave = leaveService.get(new Long(businessKey));
            results.add(leave);
//            processInstanceId = leave.getProcessInstanceId();
        }
        /**************某一次流程执行了多少步***************/
//        List<HistoricActivityInstance> list1 = processEngine.getHistoryService()
//                .createHistoricActivityInstanceQuery()
//                .processInstanceId(processInstanceId)
//                .list();
//        if (list1 != null && list1.size() > 0) {
//            for (HistoricActivityInstance hai : list1) {
//                System.out.println(hai.getId());
//                System.out.println("步骤ID：" + hai.getActivityId());
//                System.out.println("步骤名称：" + hai.getActivityName());
//                System.out.println("执行人：" + hai.getAssignee());
//                System.out.println("====================================");
//            }
//        }
        /*************某一次流程的执行经历的多少任务************/
//        List<HistoricTaskInstance> list2 = processEngine.getHistoryService()
//                .createHistoricTaskInstanceQuery()
//                .processInstanceId(processInstanceId)
//                .list();
//        if (list2 != null && list2.size() > 0) {
//            for (HistoricTaskInstance hti : list2) {
//                System.out.print("taskId:" + hti.getId() + "，");
//                System.out.print("name:" + hti.getName() + "，");
//                System.out.print("pdId:" + hti.getProcessDefinitionId() + "，");
//                System.out.print("assignee:" + hti.getAssignee() + "，");
//            }
//        }
        /**************执行中任务流程变量***************/
//        List<HistoricVariableInstance> list3 = processEngine.getHistoryService()
//                .createHistoricVariableInstanceQuery()
//                .processInstanceId(processInstanceId)
//                .list();
//        if(list3 != null && list3.size()>0) {
//            for (HistoricVariableInstance hvi : list3) {
//                System.out.print("piId:" + hvi.getProcessInstanceId() + "，");
//                System.out.print("variablesName:" + hvi.getVariableName() + "，");
//                System.out.println("variablesValue:" + hvi.getValue() + ";");
//            }
//        }
        pageInfo.setTotal(query.count());
        pageInfo.setRows(results);
        return results;
    }

    public List<LeaveApproval> getAuditHis(String leaveId) {
        return leaveService.listAllApprovalHis(leaveId);
    }
}
