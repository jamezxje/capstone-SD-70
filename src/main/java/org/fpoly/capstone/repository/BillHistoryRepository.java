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

    List<BillHistory> findByBillId(Long id);

    List<BillHistory> findAllByBill(Bill bill);

    @Query("SELECT h.id , h.status as status , h.createDate as createDate FROM BillHistory h WHERE h.status != 'TAO_HOA_DON' AND h.bill.id = :id")
    List<Object[]> findAllStatusExcludingTaoHoaDon(@Param("id") Long id);


//    @Query(value = """
//    SELECT
//        b.type,
//        bh.status,
//        bh.action_description,
//        bh.create_date,
//        u.full_name
//    FROM bill b
//    INNER JOIN bill_history bh ON bh.id_bill = b.id
//    INNER JOIN user u ON bh.id_user = u.id
//    WHERE b.id = :id
//    """, nativeQuery = true)
//    List<Object[]> find(@Param("id") Long id);
}
