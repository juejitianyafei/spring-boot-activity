package com.ty.activity.enums;


/**
 * 请假单状态 0初始录入,1.开始审批,2为审批完成
 */
public enum LeaveStatusEnum {
    INIT_INPUT(1),//初始录入
    START_APPROVAL(2),//开始审批
    END_APPROVAL(3);//审批完成

    private int value;

    LeaveStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}