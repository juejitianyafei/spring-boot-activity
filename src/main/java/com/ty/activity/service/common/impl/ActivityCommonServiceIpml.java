package com.ty.activity.service.common.impl;

import com.ty.activity.common.act.CustomProcessDiagramGenerator;
import com.ty.activity.service.common.ActivityCommonServcie;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ActivityCommonServiceIpml implements ActivityCommonServcie {


    @Autowired
    private ProcessEngine processEngine;

    @Override
    /**
     * 获取流程图片
     *
     * @param processInstanceId
     * @return
     */
    public InputStream getDiagram(String processInstanceId) {
        // 获取历史流程实例
        HistoricProcessInstance historicProcessInstance = processEngine.getHistoryService()
                .createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        // 获取流程中已经执行的节点，按照执行先后顺序排序
        List<HistoricActivityInstance> historicActivityInstances = processEngine.getHistoryService().
                createHistoricActivityInstanceQuery().processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceId().asc().list();
        // 高亮已经执行流程节点ID集合
        List<String> highLightedActivitiIds = new ArrayList<>();
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            highLightedActivitiIds.add(historicActivityInstance.getActivityId());
        }

        List<HistoricProcessInstance> historicFinishedProcessInstances = processEngine.getHistoryService()
                .createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).finished()
                .list();
        ProcessDiagramGenerator processDiagramGenerator = null;
        // 如果还没完成，流程图高亮颜色为绿色，如果已经完成为红色
        if (!CollectionUtils.isEmpty(historicFinishedProcessInstances)) {
            // 如果不为空，说明已经完成
            processDiagramGenerator = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
        } else {
            processDiagramGenerator = new CustomProcessDiagramGenerator();
        }

        BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(historicProcessInstance.getProcessDefinitionId());
        // 高亮流程已发生流转的线id集合
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl)
                processEngine.getRepositoryService()).getDeployedProcessDefinition(historicProcessInstance
                .getProcessDefinitionId());
        List<String> executedFlowIdList = executedFlowIdList(bpmnModel, processDefinitionEntity, historicActivityInstances);
        // 使用默认配置获得流程图表生成器，并生成追踪图片字符流
        InputStream imageStream = processDiagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivitiIds, executedFlowIdList, "宋体", "微软雅黑", "黑体", null, 2.0);
        return imageStream;
    }

    /**
     * 高亮流程已发生流转的线id集合
     *
     * @param bpmnModel
     * @param processDefinitionEntity
     * @param historicActivityInstanceList
     * @return
     */
    private static List<String> executedFlowIdList(BpmnModel bpmnModel, ProcessDefinitionEntity processDefinitionEntity,
                                                   List<HistoricActivityInstance> historicActivityInstanceList) {

        List<String> executedFlowIdList = new ArrayList<>();


        for (int i = 0; i < historicActivityInstanceList.size() - 1; i++) {
            HistoricActivityInstance hai = historicActivityInstanceList.get(i);
            FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement(hai.getActivityId());
            List<SequenceFlow> sequenceFlows = flowNode.getOutgoingFlows();
            if (sequenceFlows.size() > 1) {
                HistoricActivityInstance nextHai = historicActivityInstanceList.get(i + 1);
                sequenceFlows.forEach(sequenceFlow -> {
                    if (sequenceFlow.getTargetFlowElement().getId().equals(nextHai.getActivityId())) {
                        executedFlowIdList.add(sequenceFlow.getId());
                    }
                });
            } else {
                executedFlowIdList.add(sequenceFlows.get(0).getId());
            }
        }

        return executedFlowIdList;
    }
}
