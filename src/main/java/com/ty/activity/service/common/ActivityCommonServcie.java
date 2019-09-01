package com.ty.activity.service.common;

import java.io.InputStream;

public interface ActivityCommonServcie {
    /**
     * 流程图
     * @param processInstanceId
     * @return
     */
    InputStream getDiagram(String processInstanceId);
}
