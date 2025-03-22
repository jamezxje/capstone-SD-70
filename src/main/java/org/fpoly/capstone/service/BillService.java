package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.Cart;
import org.fpoly.capstone.service.payload.bill.CreateBillRequest;

import java.util.List;

public interface BillService {

    void saveToBillForOnlineUser(Cart cart, CreateBillRequest request);

    List<Bill> findBillsByCustomerId(Long customerId);

}
