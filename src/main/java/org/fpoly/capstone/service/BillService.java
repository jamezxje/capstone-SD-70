package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.service.payload.bill.BuyNowBillRequest;
import org.fpoly.capstone.service.payload.bill.CreateBillRequest;

import java.util.List;

public interface BillService {

    void saveToBillForOnlineUser(CreateBillRequest request);

    List<Bill> findBillsByCustomerId(Long customerId);

    void buyNowForOnlineUser(BuyNowBillRequest request);

    List<Bill> findLastestBillByCustomerId();

    void saveToBillForBuyNow(CreateBillRequest createBillRequest);

}
