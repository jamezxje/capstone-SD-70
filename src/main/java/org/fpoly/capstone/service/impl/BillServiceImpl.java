package org.fpoly.capstone.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.BillDetail;
import org.fpoly.capstone.entity.Cart;
import org.fpoly.capstone.entity.CartDetail;
import org.fpoly.capstone.entity.ProductDetail;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.enum_status.BillStatus;
import org.fpoly.capstone.entity.enum_status.BillType;
import org.fpoly.capstone.entity.enum_status.PaymentMethod;
import org.fpoly.capstone.repository.BillDetailRespository;
import org.fpoly.capstone.repository.BillRespository;
import org.fpoly.capstone.repository.CartRepository;
import org.fpoly.capstone.service.BillService;
import org.fpoly.capstone.service.ProductDetailService;
import org.fpoly.capstone.service.UserService;
import org.fpoly.capstone.service.payload.bill.BuyNowBillRequest;
import org.fpoly.capstone.service.payload.bill.CreateBillRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {

    private final BillDetailRespository billDetailRespository;
    private final BillRespository billRespository;
    private final CartRepository cartRepository;
    private final UserService userService;
    private final ProductDetailService productDetailService;

    @Override
    public void saveToBillForOnlineUser(CreateBillRequest request) {

        User loggedUser = this.userService.getUserFromContext();
        Cart cart = this.cartRepository.findCartByUserId(loggedUser.getId());

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

    @Override
    public void buyNowForOnlineUser(BuyNowBillRequest request) {
        User loggedUser = this.userService.getUserFromContext();

        if (loggedUser == null) {
            throw new EntityNotFoundException("User not found");
        }

        //find product detail by productId and sizeId and colorId from request
        ProductDetail productDetailRequest = this.productDetailService
                .findProductDetailByIdAndSizeAndColor(request.getProductId(), request.getSizeId(), request.getColorId());

        if (productDetailRequest == null) {
            log.error("Product detail not found for Product ID: {} and Size ID: {} and ColorId: {}", request.getProductId(), request.getSizeId(), request.getColorId());
            throw new EntityNotFoundException("Product detail not found for the given product and size and color.");
        }

        //check if quantity from request is valid or not
        if (request.getQuantity() > productDetailRequest.getQuantity()) {
            log.error("Not enough product quantity: {}",
                    request.getQuantity());
            throw new RuntimeException("Not enough quantity");
        }

        Bill bill = new Bill();

        bill.setUser(loggedUser);
        bill.setType(BillType.ONLINE);
        bill.setStatus(BillStatus.CHO_XAC_NHAN);

        double totalPrice = request.getQuantity() * productDetailRequest.getPrice().doubleValue();
        bill.setTotalMoney(BigDecimal.valueOf(totalPrice));

        List<BillDetail> billDetailList = new ArrayList<>();

        BillDetail billDetail = new BillDetail();
        billDetail.setBill(bill);
        billDetail.setProductDetail(productDetailRequest);
        billDetail.setQuantity(request.getQuantity());
        billDetail.setPrice(productDetailRequest.getPrice());
        this.billDetailRespository.save(billDetail);
        billDetailList.add(billDetail);


        bill.setBillDetailList(billDetailList);
        this.billRespository.save(bill);

    }


    @Override
    public List<Bill> findLastestBillByCustomerId() {
        User loggedUser = this.userService.getUserFromContext();

        if (loggedUser == null) {
            throw new EntityNotFoundException("User not found");
        }

        return this.billRespository.getLastestBill(loggedUser.getId(), PageRequest.ofSize(1));
    }

    @Override
    public void saveToBillForBuyNow(CreateBillRequest request) {
        List<Bill> lastestBillList = this.findLastestBillByCustomerId();

        if (lastestBillList.isEmpty()) {
            throw new EntityNotFoundException("No bill found for the user.");
        }

        Bill lastestBill = lastestBillList.get(0); // Lấy hóa đơn mới nhất

        log.info("Lastest bill id: ", lastestBill.getId());

        BigDecimal moneyShip = request.getMoneyShip();
        Date receiveDate = request.getReceiveDate();
        BigDecimal grandTotal = request.getGrandTotal();
        String address = request.getAddress();
        String note = request.getNote();
        PaymentMethod paymentMethod = request.getPaymentMethod();

        // Cập nhật các giá trị của bill
        lastestBill.setTotalMoney(grandTotal);
        lastestBill.setMoneyShip(moneyShip);
        lastestBill.setReceiveDate(receiveDate);
        lastestBill.setAddress(address);
        lastestBill.setNote(note);
        lastestBill.setMethod(paymentMethod);

        // Lưu hóa đơn đã cập nhật
        this.billRespository.save(lastestBill); // Không tạo một bill mới, chỉ cập nhật hóa đơn hiện tại
    }


}
