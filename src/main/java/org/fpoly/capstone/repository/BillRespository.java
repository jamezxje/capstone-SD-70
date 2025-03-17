package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRespository extends JpaRepository<Bill, Long> {

    @Query("SELECT b FROM Bill b where b.user.id = :customerId")
    List<Bill> getBillByCustomerId(@Param("customerId") Long customerId);
}
