package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.BillDetail;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface BillDetailService {

    List<BillDetail> findAll();

    List<BillDetail> findByCreateDate(LocalDate date);

    List<BillDetail> findByCreateDateBetween(LocalDate start, LocalDate end);

}
