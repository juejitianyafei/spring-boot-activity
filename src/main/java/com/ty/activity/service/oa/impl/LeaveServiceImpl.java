package com.ty.activity.service.oa.impl;


import com.ty.activity.dao.LeaveApprovalDao;
import com.ty.activity.dao.LeaveDao;
import com.ty.activity.domain.oa.Leave;
import com.ty.activity.domain.oa.LeaveApproval;
import com.ty.activity.service.oa.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(rollbackFor = Exception.class)
public class LeaveServiceImpl implements LeaveService {

    @Autowired
    private LeaveDao leaveDao;

    @Autowired
    private LeaveApprovalDao leaveApprovalDao;

    @Override
    public List<Leave> listAll(Long userId) {
        return leaveDao.findAllByUserId(userId);
    }

    @Override
    public Long save(Leave Leave) {
        return leaveDao.save(Leave).getId();
    }

    @Override
    public Leave get(Long id) {
        return leaveDao.findById(id).get();
    }

    @Override
    public void update(Leave Leave) {
        leaveDao.save(Leave);
    }


    @Override
    public Long saveLeaveApproval(LeaveApproval leaveApproval) {
        return leaveApprovalDao.save(leaveApproval).getId();
    }

    @Override
    public List<LeaveApproval> listAllApprovalHis(String leaveId) {
        return leaveApprovalDao.findAllByLeaveId(Long.valueOf(leaveId));
    }
}
