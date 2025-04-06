package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.BillHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BillHistoryRepository extends JpaRepository<BillHistory, Long> {
//    @Modifying
//    @Transactional
//    @Query("DELETE FROM BillHistory bh WHERE bh.bill.id = :billId")
    void deleteAllByBillId( Long billId);

    List<BillHistory> findAllByBill(Bill bill);
}
