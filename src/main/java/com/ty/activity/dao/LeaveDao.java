package com.ty.activity.dao;


import com.ty.activity.domain.oa.Leave;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LeaveDao extends CrudRepository<Leave, Long> {

    @Query(value = "select * from oa_leave where user_id = ?1", nativeQuery = true)
    List<Leave> findAllByUserId(Long userId);
}
