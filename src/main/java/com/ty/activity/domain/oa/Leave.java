package com.ty.activity.domain.oa;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "OA_LEAVE")
public class Leave implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 流程实例ID
     **/
    @Column
    private String processInstanceId;//流程实例ID

    /**
     * 请假人
     */
    @Column
    private String userId;
    /**
     * 请假人名称
     */
    @Column
    private String userName;

    /**
     * 请假开始时间
     */

    @Column(name = "START_TIME")
    private Date startTime;

    /**
     * 请假结束时间
     */
    @Column(name = "END_TIME")
    private Date endTime;

    /**
     * 提交申请时间
     */
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date applyTime;
    /**
     * 请假类型:1公休 2病假 3调休 4事假 5婚假
     */
    @Column
    private String leaveType;
    /**
     * 请假原因
     */
    @Column
    private String reason;

    //-- 临时属性 --//
    @Transient
    private String processDefinitionId;//流程定义流程定义processDefinition.ID
    @Transient
    private Integer processDefinitionVersion;//流程定义processDefinition.version
    @Transient
    private String taskId;//流程定义task.id
    @Transient
    private String taskName;//流程定义task.name
    @Transient
    private String assignee;//流程定义task.assignee当前流程办理人
    @Transient
    private Date taskCreateTime;//流程定义task.createTime
    @Transient
    private Boolean processDefinitionSuspended;//流程定义流程定义processDefinition.suspended
    @Transient
    private String taskDefinitionKey;//当前流程节点
}
