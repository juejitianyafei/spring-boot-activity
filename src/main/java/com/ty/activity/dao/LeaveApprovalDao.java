package com.ty.activity.dao;


import com.ty.activity.domain.oa.LeaveApproval;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LeaveApprovalDao extends CrudRepository<LeaveApproval,Long> {

    @Query(value="select * from oa_leave_approval where leave_id = ?1",nativeQuery=true)
    List<LeaveApproval> findAllByLeaveId(Long leaveId);
}
