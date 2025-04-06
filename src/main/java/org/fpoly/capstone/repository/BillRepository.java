package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import org.fpoly.capstone.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    @Query(value = """
            SELECT * FROM Bill b where b.status = 'TAO_HOA_DON'
            """, nativeQuery = true)
    List<Bill> getBillTAOHOADON();
    Optional<Bill> findById(Long id);

    void deleteAllById(Long id);
    @Query(value = """
SELECT b.id FROM Bill b where b.vnpTransaction = :code
""")
    List<String> findAllByVnpTransaction(@Param("code") String code);
    Optional<Bill> findByCode(String code);
    @Query("select b.id from Bill b")
    List<Long> findByAllIds();
    Page<Bill> findAll(Pageable pageable);


}
