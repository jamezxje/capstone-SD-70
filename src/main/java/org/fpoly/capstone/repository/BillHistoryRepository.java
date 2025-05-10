package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.BillHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillHistoryRepository extends JpaRepository<BillHistory, Long> {
    void deleteAllByBillId(Long billId);
    List<BillHistory> findAllByBill(Bill bill);
}
