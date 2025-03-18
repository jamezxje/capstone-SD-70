package org.fpoly.capstone.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.repository.BillRepository;
import org.fpoly.capstone.service.BillService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;

    @Override
    public List<Long> findAllById() {
        return billRepository.findByAllIds();
    }

    @Override
    public Bill findById(Long id) {
        Bill bill = billRepository.findById(id).orElseThrow();
        return bill;
    }
}
