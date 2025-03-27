package org.fpoly.capstone.repository;


import org.fpoly.capstone.entity.VoucherDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherDetailRepository extends JpaRepository<VoucherDetail, Long> {
    List<VoucherDetail> findByBillId(Long id);

    @Query(value = """
            SELECT vd.before_price, 
                   vd.after_price, 
                   vd.discount_price
            FROM voucher_detail vd
            INNER JOIN bill b ON vd.id_bill = b.id
            WHERE b.id = :id
            """, nativeQuery = true)
    List<Object[]> findPricesByBillId(@Param("id") Long id);
}
