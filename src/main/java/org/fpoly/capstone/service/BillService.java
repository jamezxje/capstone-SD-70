package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.Bill;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface BillService {

    List<Long> findAllById();

    Bill findById(Long id);

    List<Bill> findByCreateDate(LocalDate date);

    List<Bill> findAll();

    List<Bill> findByCreateDateBetween(LocalDate start, LocalDate end);
}
