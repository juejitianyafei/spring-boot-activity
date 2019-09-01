package com.ty.activity.service.oa;


import com.ty.activity.domain.oa.Leave;
import com.ty.activity.domain.oa.LeaveApproval;

import java.util.List;

public interface LeaveService {
    List<Leave> listAll(Long userId);

    Long save(Leave Leave);

    Leave get(Long id);

    void update(Leave Leave);

    Long saveLeaveApproval(LeaveApproval leaveApproval);

    List<LeaveApproval> listAllApprovalHis(String leaveId);
}
