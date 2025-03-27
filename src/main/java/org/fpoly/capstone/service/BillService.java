package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;

public interface BillService {
    Page<Bill> searchBills(String keyword, String orderType, LocalDate startDate, LocalDate endDate, Pageable pageable);
}
