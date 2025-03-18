package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    @Query("select b.id from Bill b")
    List<Long> findByAllIds();
}
