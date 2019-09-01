package com.ty.activity.domain.oa;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "OA_LEAVE_APPROVAL")
public class LeaveApproval implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 请假单ID
     */
    @Column
    private Long leaveId;
    /**
     * 流程实例ID
     */
    @Column
    private String processInstanceId;//流程实例ID

    /**
     * 当前任务节点
     */
    @Column
    private String currentTaskNode;

    /**
     * 审批结果
     */
    @Column
    private Boolean approvalResult;

    /**
     * 审批意见
     */
    @Column
    private String approvalSuggestion;

    /**
     * 办理人
     */
    @Column
    private String assignee;

    /**
     * 节点操作时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_TIME")
    private Date oprateTime;

}
