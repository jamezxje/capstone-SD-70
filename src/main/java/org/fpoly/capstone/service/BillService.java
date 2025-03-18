package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.Bill;

import java.util.List;

public interface BillService {

    List<Long> findAllById();

    Bill findById(Long id);

}
