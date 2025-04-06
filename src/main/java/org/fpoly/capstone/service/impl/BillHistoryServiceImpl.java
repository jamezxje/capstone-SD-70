package org.fpoly.capstone.service.impl;

import org.fpoly.capstone.entity.BillHistory;
import org.fpoly.capstone.repository.BillHistoryRepository;
import org.fpoly.capstone.service.BillHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BillHistoryServiceImpl implements BillHistoryService {

    @Autowired
    private BillHistoryRepository billHistoryRepository;

    @Override
    public List<BillHistory> getHistoryByBillId(Long billId) {
        return billHistoryRepository.findByBillId(billId);
    }
}
