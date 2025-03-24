package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    @Query("select b.id from Bill b")
    List<Long> findByAllIds();

//    List<Bill> findByBillDate(LocalDate billDate);


    List<Bill> findByCreateDate(Date createDate);

    @Query("SELECT b FROM Bill b WHERE b.createDate BETWEEN :startOfDay AND :endOfDay")
    List<Bill> findByCreateDateBetween(@Param("startOfDay") Date startOfDay, @Param("endOfDay") Date endOfDay);
}
