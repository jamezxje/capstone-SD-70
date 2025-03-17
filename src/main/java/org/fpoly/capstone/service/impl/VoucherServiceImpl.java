package org.fpoly.capstone.service.impl;

import org.fpoly.capstone.entity.Voucher;
import org.fpoly.capstone.repository.VoucherRepository;
import org.fpoly.capstone.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoucherServiceImpl implements VoucherService {
    @Override
    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }

    @Autowired
    private VoucherRepository voucherRepository;

}
