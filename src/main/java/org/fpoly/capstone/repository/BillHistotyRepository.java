package org.fpoly.capstone.repository;


import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.BillHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillHistotyRepository extends JpaRepository<BillHistory, Long> {
    List<BillHistory> findByBillId(Long id);

    List<BillHistory> findAllByBill(Bill bill);

    @Query("SELECT h.id , h.status as status , h.createDate as createDate FROM BillHistory h WHERE h.status != 'TAO_HOA_DON' AND h.bill.id = :id")
    List<Object[]> findAllStatusExcludingTaoHoaDon(@Param("id") Long id);

}
