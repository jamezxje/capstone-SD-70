package org.fpoly.capstone.repository;


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

    Page<Bill> findAll(Pageable pageable);

}
