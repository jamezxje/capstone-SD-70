package org.fpoly.capstone.service;

import jakarta.mail.MessagingException;
import org.fpoly.capstone.dto.address.BaseAddressRequest;
import org.fpoly.capstone.dto.bill.CreateBillCustomerOnlineRequest;
import org.fpoly.capstone.dto.bill.CreateBillOfflineDTO;
import org.fpoly.capstone.dto.bill.CreateCustomerBill;
import org.fpoly.capstone.dto.bill.GetAllCusomter;
import org.fpoly.capstone.dto.bill.ProductRequest;
import org.fpoly.capstone.dto.bill.VoucherRequest1;
import org.fpoly.capstone.dto.billDetail.BillProductDTO;
import org.fpoly.capstone.dto.voucher.VoucherRequest;
import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.Brand;
import org.fpoly.capstone.entity.Category;
import org.fpoly.capstone.entity.Color;
import org.fpoly.capstone.entity.Material;
import org.fpoly.capstone.entity.ProductDetail;
import org.fpoly.capstone.entity.Size;
import org.fpoly.capstone.entity.Voucher;
import org.fpoly.capstone.entity.enum_status.BillStatus;
import org.fpoly.capstone.entity.enum_status.BillType;
import org.fpoly.capstone.service.payload.bill.BuyNowBillRequest;
import org.fpoly.capstone.service.payload.bill.CreateBillDetailFromCartRequest;
import org.fpoly.capstone.service.payload.bill.CreateBillRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BillService {
    CreateBillRequest create(CreateBillRequest createBillDTO);

    Bill createBillCode(Long idEmployee);

    List<Bill> getBillTaoHoaDon();

    Bill deleteBill(Long id);

    Bill save(Long id, CreateBillOfflineDTO request);

    List<BillProductDTO> getBillDetail(Long billId);

    List<BillProductDTO> getBillDetailCustomer(String code);

    Bill saveProductInBill(Long id, CreateBillOfflineDTO request);

    Bill deleteProductInBill(Long idBill, Long idProduct);

    List<BaseAddressRequest> getAllAddressUser(Long idUser);

    List<VoucherRequest> getVoucherMinimumbill(Integer minimumBill);

    List<VoucherRequest> getVoucherMiniNoLogin(Integer miniNoLogin);

    Page<ProductRequest> findAllProductDetail(int page, int size);

    List<Voucher> getAllVoucher();

    CreateCustomerBill createCustomerBill(CreateCustomerBill createCustomerBill);

    Page<GetAllCusomter> findALlCustomerPage(int page, int size);

    Page<VoucherRequest1> findAllVoucherPage(Integer totalAmount, int page, int size);

    List<GetAllCusomter> searchCustomer(String searchQuery);

    Page<ProductRequest> searchProduct(String name, Long category, Long color, Long material, Long kichCo, Long brand, int page, int size);

    List<Brand> findAllBrand();

    List<Size> findAllSize();

    List<Category> findAllCategory();

    List<Material> findAllMaterial();

    List<Color> findAllColor();

    List<Long> findAllById();


    List<Bill> findByCreateDate(LocalDate date);

    List<Bill> findAll();

    List<Bill> findByCreateDateBetween(LocalDate start, LocalDate end);

    Bill findById(Long id);

    void saveToBillForOnlineUser(CreateBillRequest request);

    void saveToBillForOnlineUserVnPay(CreateBillRequest request);

    void saveToBillForOnlineUserSelectFromCart(List<CreateBillDetailFromCartRequest> createBillDetailFromCartRequests, CreateBillRequest request);

    void buyNowForOnlineUser(BuyNowBillRequest request);

    void saveToBillForBuyNow(CreateBillRequest createBillRequest);

    List<Bill> findBillsByCustomerId(Long customerId);

    Page<Bill> searchBills(String keyword, BillType orderType, BillStatus status,
                           LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    List<Bill> findLastestBillByCustomerId();

    Optional<Bill> searchCode(String code);

    void checkoutFormCart(List<Long> selectedCartDetailIds);

    Page<Bill> searchBillsWithStatuses(String keyword,
                                       BillType billType,
                                       List<BillStatus> statuses,
                                       LocalDateTime startDate,
                                       LocalDateTime endDate,
                                       Pageable pageable);

    Bill createBillOnlieCustomerRequest(CreateBillCustomerOnlineRequest request) throws MessagingException;

    List<Bill> findCancelledBillsDate(LocalDate date);

    List<Bill> findByCancelledDateBetween(LocalDate start, LocalDate end);


    Optional<ProductDetail> finProductDetailById(Long idProduct, Long idSize, Long idColor);

    Optional<ProductDetail> findByIDProductDetail(Integer idProductDetail);
}
