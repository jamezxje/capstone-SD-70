package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.enum_status.BillStatus;
import org.fpoly.capstone.entity.enum_status.BillType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    @Query(value = """
            SELECT * FROM Bill b where b.status = 'TAO_HOA_DON'
            """, nativeQuery = true)
    List<Bill> getBillTAOHOADON();

    @Override
    Optional<Bill> findById(Long id);

    void deleteAllById(Long id);

    @Query(value = """
            SELECT b.id FROM Bill b where b.vnpTransaction = :code
            """)
    List<String> findAllByVnpTransaction(@Param("code") String code);

    Optional<Bill> findByCode(String code);

    @Query("select b.id from Bill b")
    List<Long> findByAllIds();

    @Override
    Page<Bill> findAll(Pageable pageable);


    @Query("SELECT b FROM Bill b where b.user.id = :customerId ORDER BY b.createDate DESC")
    List<Bill> getBillByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT b FROM Bill  b where b.user.id = :customerId ORDER BY b.createDate DESC ")
    List<Bill> getLastestBill(@Param("customerId") Long customerId, Pageable pageable);

    @Query("SELECT b FROM Bill b WHERE b.createDate BETWEEN :startOfDay AND :endOfDay")
    List<Bill> findByCreateDateBetween(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT b FROM Bill b WHERE b.confirmationDate BETWEEN :startOfDay AND :endOfDay")
    List<Bill> findCancelledBillsByDate(@Param("startOfDay") LocalDateTime startOfDay,
                                        @Param("endOfDay") LocalDateTime endOfDay);



    @Query("SELECT b FROM Bill b " +
            "WHERE (:keyword IS NULL OR b.code LIKE %:keyword%) " +
            "AND (:orderType IS NULL OR b.type = :orderType) " +
            "AND (:status IS NULL OR b.status = :status) " +
            "AND (:startDate IS NULL OR b.createDate >= :startDate) " +
            "AND (:endDate IS NULL OR b.createDate <= :endDate)")
    Page<Bill> searchBills(@Param("keyword") String keyword,
                           @Param("orderType") BillType orderType,
                           @Param("status") BillStatus status,
                           @Param("startDate") LocalDateTime startDate,
                           @Param("endDate") LocalDateTime endDate,
                           Pageable pageable);

    @Query("SELECT b FROM Bill b " +
            "WHERE b.status IN :statuses " +
            "AND (:keyword IS NULL OR b.code LIKE %:keyword% OR b.userName LIKE %:keyword%) " +
            "AND (:type IS NULL OR b.type = :type) " +
            "AND (:startDate IS NULL OR b.createDate >= :startDate) " +
            "AND (:endDate IS NULL OR b.createDate <= :endDate)")
    Page<Bill> findByMultipleStatuses(
            @Param("statuses") List<BillStatus> statuses,
            @Param("keyword") String keyword,
            @Param("type") BillType type,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

}