package org.fpoly.capstone.repository;

import org.fpoly.capstone.dto.voucher.VoucherRequest;
import org.fpoly.capstone.entity.BillDetail;
import org.fpoly.capstone.entity.Voucher;
import org.fpoly.capstone.entity.enum_status.VoucherStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    List<Voucher> findAll();
    @Query("""
    SELECT v.id as id , 
           v.code as code ,
           v.name as name ,
           v.value as value ,
           v.minimumBill as minimumBill ,
           v.quantity as quantity ,
           v.startDate as startDate ,
           v.endDate as endDate 
    FROM Voucher v 
    WHERE v.status = 'ACTIVE' 
      AND v.minimumBill <= :totalAmount
    ORDER BY v.lastModifiedDate DESC
""")
    Page<Object[]> findAllVoucherRequests(@Param("totalAmount") int totalAmount, Pageable pageable);

    @Query("""
    SELECT 
        v.id as id,
        v.code as code,
        v.name as name,
        v.value as value,
        v.minimumBill as minimumBill,
        v.quantity as quantity,
        v.startDate as startDate,
        v.endDate as endDate,
        v.status as status
    FROM Voucher v
    WHERE v.status = 'ACTIVE' AND v.minimumBill <= :minimumBill
    ORDER BY v.minimumBill DESC
    LIMIT 1
""")
    List<Object[]> getVoucherMinimumBill(@Param("minimumBill") int minimumBill);

    @Query("""
    SELECT 
        v.id as id,
        v.code as code,
        v.name as name,
        v.value as value,
        v.minimumBill as minimumBill,
        v.quantity as quantity,
        v.startDate as startDate,
        v.endDate as endDate,
        v.status as status
    FROM Voucher v
    WHERE v.status = 'ACTIVE' AND v.minimumBill <= :minimumBill
    ORDER BY v.minimumBill DESC
  
""")
    List<Object[]> getVoucherMinimumBillNoLogin(@Param("minimumBill") int minimumBill);

    @Query("select v from Voucher v order by v.lastModifiedDate desc")
    Page<Voucher> findAll(Pageable pageable);


    @Query("SELECT v FROM Voucher v WHERE v.name = :name or v.status = :status")
    Page<Voucher> searchNameOrStatus(Pageable pageable, @Param("name") String name ,@Param("status") VoucherStatus status);

    @Query("SELECT v FROM Voucher v " +
            "WHERE (:name IS NULL OR v.name = :name) " +
            "AND (:status IS NULL OR v.status = :status) " +
            "AND (:startDate IS NULL OR v.startDate >= :startDate) " +
            "AND (:endDate IS NULL OR v.endDate <= :endDate)")
    Page<Voucher> searchByStartDateAndEndDate(Pageable pageable,
                         @Param("name") String name,
                         @Param("status") VoucherStatus status,
                         @Param("startDate") LocalDateTime startDate,
                         @Param("endDate") LocalDateTime endDate);


    @Query("select v.id from Voucher v")
    List<Long> findByAllIds();

    Voucher findByCode(String code);

    @Query("SELECT v FROM Voucher v WHERE v.status = 'ACTIVE' ORDER BY v.value DESC")
    List<Voucher> findAllActiveVouchers();

    @Query("SELECT v FROM Voucher v WHERE v.name = :name")
    Voucher findByName(@Param("name") String name);

    @Query("SELECT v.name FROM Voucher v")
    List<String> findAllNames();

}
