package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface BillDetailRepository extends JpaRepository<BillDetail, Long> {

    List<BillDetail> findByCreateDate(Date date);

    @Query("SELECT bd FROM BillDetail bd WHERE bd.createDate BETWEEN :startOfDay AND :endOfDay")
    List<BillDetail> findByCreateDateBetween(@Param("startOfDay") Date startOfDay, @Param("endOfDay") Date endOfDay);



}
