package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRespository extends JpaRepository<Bill, Long> {
}
