package org.fpoly.capstone.service.impl;

import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.BillDetail;
import org.fpoly.capstone.entity.Cart;
import org.fpoly.capstone.entity.CartDetail;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.enum_status.BillStatus;
import org.fpoly.capstone.entity.enum_status.BillType;
import org.fpoly.capstone.entity.enum_status.PaymentMethod;
import org.fpoly.capstone.repository.BillDetailRespository;
import org.fpoly.capstone.repository.BillRespository;
import org.fpoly.capstone.repository.CartRepository;
import org.fpoly.capstone.service.BillService;
import org.fpoly.capstone.service.payload.bill.CreateBillRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {

    private final BillDetailRespository billDetailRespository;
    private final BillRespository billRespository;
    private final CartRepository cartRepository;

    @Override
    public void saveToBillForOnlineUser(Cart cart, CreateBillRequest request) {
        Bill bill = new Bill();

        User customer = cart.getUser();
        bill.setUser(customer);
        bill.setType(BillType.ONLINE);
        bill.setStatus(BillStatus.CHO_XAC_NHAN);

        double totalPrice = cart.getCartDetails().stream()
                .mapToDouble(detail -> detail.getPrice().doubleValue() * detail.getQuantity())
                .sum();
        bill.setTotalMoney(BigDecimal.valueOf(totalPrice));

        List<BillDetail> billDetailList = new ArrayList<>();
        for (CartDetail cartDetail : cart.getCartDetails()) {
            BillDetail billDetail = new BillDetail();
            billDetail.setBill(bill);
            billDetail.setProductDetail(cartDetail.getProductDetail());
            billDetail.setQuantity(cartDetail.getQuantity());
            billDetail.setPrice(cartDetail.getPrice());
            this.billDetailRespository.save(billDetail);
            billDetailList.add(billDetail);
        }

        BigDecimal moneyShip = request.getMoneyShip();
        Date recieveDate = request.getReceiveDate();
        BigDecimal grandTotal = request.getGrandTotal();
        String address = request.getAddress();
        String note = request.getNote();
        PaymentMethod paymentMethod = request.getPaymentMethod();

        bill.setTotalMoney(grandTotal);
        bill.setMoneyShip(moneyShip);
        bill.setReceiveDate(recieveDate);
        bill.setAddress(address);
        bill.setNote(note);
        bill.setMethod(paymentMethod);

        bill.setBillDetailList(billDetailList);
        this.cartRepository.deleteById(cart.getId());
        this.billRespository.save(bill);

    }

    @Override
    public List<Bill> findBillsByCustomerId(Long customerId) {
        return this.billRespository.getBillByCustomerId(customerId);
    }

}
