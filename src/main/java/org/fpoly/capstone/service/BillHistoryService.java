package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.BillHistory;

import java.util.List;

public interface BillHistoryService {
    List<BillHistory> getHistoryByBillId(Long billId);
}
