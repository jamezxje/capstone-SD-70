package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.VoucherDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.fpoly.capstone.entity.VoucherDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface VoucherDetailRepository extends JpaRepository<VoucherDetail, Long> {
    Optional<VoucherDetail> findByBillId(Long id);

    @Query(value = """
            SELECT vd.before_price,\s
                   vd.after_price,\s
                   vd.discount_price,
                   b.money_ship
            FROM voucher_detail vd
            INNER JOIN bill b ON vd.id_bill = b.id
            WHERE b.id = :id;
            """, nativeQuery = true)
    List<Object[]> findPricesByBillId(@Param("id") Long id);

    @Query(value = """
            SELECT vd.before_price,\s
                   vd.after_price,\s
                   vd.discount_price,
                   b.money_ship
            FROM voucher_detail vd
            INNER JOIN bill b ON vd.id_bill = b.id
            WHERE b.code = :code;
            """, nativeQuery = true)
    List<Object[]> findPriceForBillCodeCustomer(@Param("code") String code);

    Optional<VoucherDetail> findByBill_Id(Long id);


}
