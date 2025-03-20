package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.Cart;

import java.util.List;

public interface BillService {

    void saveToBillForOnlineUser(Cart cart);

    List<Bill> findBillsByCustomerId(Long customerId);

}
