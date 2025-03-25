package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.BillHistory;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.enum_status.BillStatus;
import org.fpoly.capstone.entity.enum_status.BillType;
import org.fpoly.capstone.repository.BillHistotyRepository;
import org.fpoly.capstone.repository.BillRepository;
import org.fpoly.capstone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    public BillService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public Page<Bill> searchBills(String keyword, String orderType, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Bill bill = new Bill();

        if (keyword != null && !keyword.isEmpty()) {
            bill.setCode(keyword);
        }
        if (orderType != null && !orderType.isEmpty()) {
            try {
                bill.setType(BillType.valueOf(orderType.toUpperCase())); // Chuyển đổi Enum
            } catch (IllegalArgumentException e) {
                // Nếu nhập sai loại, bỏ qua điều kiện này
            }
        }

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withMatcher("code", ExampleMatcher.GenericPropertyMatchers.contains());

        Example<Bill> example = Example.of(bill, matcher);

        return billRepository.findAll(example, pageable);
    }

}
