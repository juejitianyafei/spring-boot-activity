package com.ty.activity.enums;

public enum UserStatusEnum {

    POST(1),//在职
    LEAVE(2);//离职

    private int code;

    UserStatusEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
