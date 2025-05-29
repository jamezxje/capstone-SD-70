package org.fpoly.capstone.service.impl;

import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.dto.voucherdetail.VoucherPriceDTO;
import org.fpoly.capstone.service.BillDetailService;
import org.fpoly.capstone.service.payload.bill_detail.BillDetailResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.fpoly.capstone.dto.billDetail.BillDetailDTO;
import org.fpoly.capstone.dto.billDetail.ChangeStatusBillRequest;
import org.fpoly.capstone.dto.billDetail.StatusBillDetailRequest;
import org.fpoly.capstone.dto.billDetail.UpdateInForCustomer;
import org.fpoly.capstone.entity.*;
import org.fpoly.capstone.entity.enum_status.BillStatus;
import org.fpoly.capstone.entity.enum_status.ProductVariantStatus;
import org.fpoly.capstone.entity.enum_status.UserRole;
import org.fpoly.capstone.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class BillDetailServiceImpl implements BillDetailService {

    @Autowired
    private BillRepository billRepository;
    @Autowired
    private BillDetailRepository billDetailRepository;
    @Autowired
    private BillHistoryRepository billHistoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductDetailRepository productDetailRepository;
    @Autowired
    private VoucherDetailRepository voucherDetailRepository;

    @Override
    public List<BillDetailResponse> findBillDetailByBillId(Long billId) {
        return this.billDetailRepository.findBillDetailByBillId(billId);
    }


    private static final Logger LOGGER = Logger.getLogger(BillDetailServiceImpl.class.getName());

    public List<BillDetailDTO> getBillDetails(Long billId) {
        List<Object[]> results = this.billDetailRepository.getProductByIDBill(billId);
        List<BillDetailDTO> billDetails = new ArrayList<>();
        for (Object[] result : results) {
            Long id = (Long) result[0];
            String name = (String) result[1];
            BigDecimal price = (BigDecimal) result[2];
            Integer quantity = (Integer) result[3];
            String size = (String) result[4];
            String color = (String) result[5];
            Long idProductDetail = (Long) result[6];
            String image = (String) result[7];
            BillDetailDTO BillDetailDTO = new BillDetailDTO(id, name, price, quantity, size, color, idProductDetail, image);
            billDetails.add(BillDetailDTO);
        }
        return billDetails;
    }

    @Override
    public Bill changeStatusBill(Long id, Long idEmployess, ChangeStatusBillRequest request) {
        Optional<Bill> bill = billRepository.findById(id);
        Optional<User> user = userRepository.findById(idEmployess);
        if (!bill.isPresent()) {
            throw new RuntimeException("Bill not found");
        }
        if (!user.isPresent()) {
            throw new RuntimeException("User not found");
        }
        boolean checkDaThanhToan = billHistoryRepository.findAllByBill(bill.get()).stream()
                .anyMatch(invoice -> invoice.getStatus() == BillStatus.DA_THANH_TOAN);
        BillStatus statusBill[] = BillStatus.values();
        int nextIndex = (bill.get().getStatus().ordinal() + 1) % statusBill.length;
        bill.get().setStatus(BillStatus.valueOf(statusBill[nextIndex].name()));
        if (nextIndex > 6) {
            throw new RuntimeException("Bill status is " + statusBill[nextIndex].name());
        }
        if (bill.get().getStatus() == BillStatus.XAC_NHAN) {
            System.out.println("Chay vao xac nhan");

            Long id_bill = bill.get().getId();

            List<BillDetail> billDetalOnlineList = billDetailRepository.findByBillId(id_bill);

            System.out.println("BillDetails found: " + billDetalOnlineList.size()); // Logging số lượng BillDetail

            if (!billDetalOnlineList.isEmpty()) {
                for (BillDetail billDetail : billDetalOnlineList) {
                    Long productDetailId = billDetail.getProductDetail().getId();
                    Optional<ProductDetail> productDetail = productDetailRepository.findById(productDetailId);

                    if (productDetail.isPresent()) {
                        ProductDetail product = productDetail.get();
                        System.out.println("Product ID: " + product.getId());
                        System.out.println("Current Quantity: " + product.getQuantity());
                        System.out.println("Product Status: " + product.getStatus());


                        if (product.getQuantity() < billDetail.getQuantity()) {
                            throw new RuntimeException("Số lượng sản phẩm không đủ để bán");
                        }

                        if (product.getStatus() != ProductVariantStatus.DANG_SU_DUNG) {
                            throw new RuntimeException("Sản phẩm không hợp lệ. Trạng thái không phải DANG_SU_DUNG.");
                        }

                        product.setQuantity(product.getQuantity() - billDetail.getQuantity());

                        if (product.getQuantity() == 0) {
                            product.setStatus(ProductVariantStatus.HET_SAN_PHAM);
                        }

                        productDetailRepository.save(product);
                        System.out.println("Product saved with updated quantity: " + product.getQuantity());
                    } else {
                        System.out.println("Product not found for ID: " + productDetailId); // Logging khi không tìm thấy sản phẩm
                    }
                }
            } else {
                System.out.println("No BillDetail found for Bill ID: " + id_bill); // Logging khi không có BillDetail
            }
            // Cập nhật ngày hoàn thành
            bill.get().setCompletionDate(Calendar.getInstance().getTime());
            System.out.println("Bill status updated to XAC_NHAN, Completion date set.");
        } else if (bill.get().getStatus() == BillStatus.DA_THANH_TOAN) {
            bill.get().setReceiveDate(Calendar.getInstance().getTime());
            if (checkDaThanhToan) {
                bill.get().setStatus(BillStatus.THANH_CONG);
                bill.get().setCompletionDate(getCurrentTimestampInVietnam());
            }
        }
        bill.get().setLastModifiedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        bill.get().setEmployee(user.get());
        BillHistory billHistory = new BillHistory();
        billHistory.setBill(bill.get());
        billHistory.setStatus(bill.get().getStatus());
        billHistory.setActionDescription(request.getActionDescription());
        billHistory.setUser(user.get());
        billHistoryRepository.save(billHistory);
        Bill billResponse = billRepository.save(bill.get());
        return billResponse;
    }

    @Override
    public List<StatusBillDetailRequest> getStatusBillHistory(Long id) {
        List<Object[]> results = billHistoryRepository.findAllStatusExcludingTaoHoaDon(id);
        List<StatusBillDetailRequest> requests = new ArrayList<>();
        for (Object[] result : results) {
            Long id_bill = (Long) result[0];
            BillStatus status = (BillStatus) result[1];
            Date createDate = (Date) result[2];
            StatusBillDetailRequest request = new StatusBillDetailRequest(id_bill, status, createDate);
            requests.add(request);

        }
        return requests;
    }

    @Override
    public List<StatusBillDetailRequest> getStatusBillHistoryCustomer(String code) {
        List<Object[]> results = billHistoryRepository.findAllStatusSearchCustomer(code);
        List<StatusBillDetailRequest> requests = new ArrayList<>();
        for (Object[] result : results) {
            Long id_bill = (Long) result[0];
            BillStatus status = (BillStatus) result[1];
            Date createDate = (Date) result[2];
            StatusBillDetailRequest request = new StatusBillDetailRequest(id_bill, status, createDate);
            requests.add(request);

        }
        return requests;
    }


    @Override
    public Bill getInforBillId(Long id) {
        return billRepository.findById(id).get();
    }

    @Override
    public Bill getInForBillCustomer(String code) {
        return billRepository.findByCode(code).get();
    }

    @Override
    public Bill updateInforBill(Long id, UpdateInForCustomer request) {
        System.out.println("Check vào đây abc");
        Optional<Bill> optionalBill = billRepository.findById(id);
        if (!optionalBill.isPresent()) {
            throw new RuntimeException("Bill not found for ID: " + id);
        }
        ;
        Bill bill = optionalBill.get();
        bill.setUserName(request.getCustomerName());
        bill.setPhoneNumber(request.getNumberPhone());
        bill.setAddress(request.getCustomerAddress());
        bill.setShipDate(request.getShipDate());
        bill.setMoneyShip(new BigDecimal(request.getMoneyShip()));


        billRepository.save(bill);

        Optional<VoucherDetail> optional = voucherDetailRepository.findByBill_Id(id);

        if (optional.isPresent()) {
            VoucherDetail voucherDetail = optional.get();
            BigDecimal totalMoney = bill.getTotalMoney() != null ? bill.getTotalMoney() : BigDecimal.ZERO;
            BigDecimal moneyShip = bill.getMoneyShip() != null ? bill.getMoneyShip() : BigDecimal.ZERO;
            BigDecimal itemDiscount = bill.getItemDiscount() != null ? bill.getItemDiscount() : BigDecimal.ZERO;

            BigDecimal afterPrice = totalMoney.add(moneyShip.subtract(itemDiscount));
            voucherDetail.setAfterPrice(afterPrice);

            voucherDetailRepository.save(voucherDetail);
            System.out.println("Đã cập nhật VoucherDetail id: " + voucherDetail.getId());
        } else {
            System.out.println("VoucherDetail không tồn tại với Bill ID: " + id + ", vui lòng kiểm tra dữ liệu.");
        }

        return bill;
    }

    @Override
    public Bill cancelBillAdmin(Long id, Long idEmployess, ChangeStatusBillRequest request) {
        Optional<Bill> bill = billRepository.findById(id);
        System.out.println("Check id bill" + bill.get().getId());
        Optional<User> user = userRepository.findById(idEmployess);
        System.out.println("Chekc id user" + user.get().getId());
        System.out.println("Check role" + user.get().getRoles());
        if (!bill.isPresent()) {
            throw new RuntimeException("Bill not found for ID: " + id);
        }
        if (!user.isPresent()) {
            throw new RuntimeException("User not found for ID: " + idEmployess);
        }
        if (user.get().getRoles() != UserRole.ROLE_ADMIN) {
            throw new RuntimeException("User is not admin");
        }
        if (bill.get().getStatus() == BillStatus.VAN_CHUYEN && user.get().getRoles() != UserRole.ROLE_ADMIN) {
            throw new RuntimeException("User is not admin and Van chuyen No cacel");
        }
        if (bill.get().getStatus() == BillStatus.XAC_NHAN) {
            System.out.println("Chay vao day");
            Long idBill = bill.get().getId();
            List<BillDetail> billDetailOnlineList = billDetailRepository.findByBillId(idBill);
            if (!billDetailOnlineList.isEmpty()) {
                for (BillDetail billDetail : billDetailOnlineList) {
                    Long productDetailId = billDetail.getProductDetail().getId();
                    Optional<ProductDetail> productDetail = productDetailRepository.findById(productDetailId);
                    if (productDetail.isPresent()) {
                        ProductDetail product = productDetail.get();
                        product.setQuantity(product.getQuantity() + billDetail.getQuantity());

                        if (product.getStatus() == ProductVariantStatus.HET_SAN_PHAM) {
                            product.setStatus(ProductVariantStatus.DANG_SU_DUNG);
                        }
                        productDetailRepository.save(product);
                    }
                }
            }
        }
        bill.get().setLastModifiedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        bill.get().setStatus(BillStatus.DA_HUY);
        bill.get().setEmployee(user.get());
        BillHistory billHistory = new BillHistory();
        billHistory.setBill(bill.get());
        billHistory.setStatus(bill.get().getStatus());
        billHistory.setActionDescription(request.getActionDescription());
        billHistory.setUser(user.get());
        billHistoryRepository.save(billHistory);
        billRepository.save(bill.get());
        return bill.get();
    }

    @Override
    public Bill cancelBillCustomer(String code, Long idCusomter, ChangeStatusBillRequest request) {
        System.out.println("Checkl");
        Optional<Bill> bill = billRepository.findByCode(code);
        Optional<User> user = userRepository.findById(idCusomter);
        if (!bill.isPresent()) {
            throw new RuntimeException("Bill not found for ID: " + code);
        }
        if (!user.isPresent()) {
            throw new RuntimeException("User not found for ID: " + idCusomter);
        }

        if (bill.get().getStatus() == BillStatus.XAC_NHAN) {
            System.out.println("Chay vao day");
            Long idBill = bill.get().getId();
            List<BillDetail> billDetailOnlineList = billDetailRepository.findByBillId(idBill);
            if (!billDetailOnlineList.isEmpty()) {
                for (BillDetail billDetail : billDetailOnlineList) {
                    Long productDetailId = billDetail.getProductDetail().getId();
                    Optional<ProductDetail> productDetail = productDetailRepository.findById(productDetailId);
                    if (productDetail.isPresent()) {
                        ProductDetail product = productDetail.get();
                        product.setQuantity(product.getQuantity() + billDetail.getQuantity());

                        if (product.getStatus() == ProductVariantStatus.HET_SAN_PHAM) {
                            product.setStatus(ProductVariantStatus.DANG_SU_DUNG);
                        }
                        productDetailRepository.save(product);
                    }
                }
            }
        }
        bill.get().setLastModifiedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        bill.get().setStatus(BillStatus.DA_HUY);
        bill.get().setEmployee(user.get());
        BillHistory billHistory = new BillHistory();
        billHistory.setBill(bill.get());
        billHistory.setStatus(bill.get().getStatus());
        billHistory.setActionDescription(request.getActionDescription());
        billHistory.setUser(user.get());
        billHistoryRepository.save(billHistory);
        billRepository.save(bill.get());
        return bill.get();
    }

    private Date getCurrentTimestampInVietnam () {
        Instant instant = Instant.now();
        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        long timestamp = instant.atZone(zoneId).toEpochSecond() * 1000;
        return new Date(timestamp);
    }
    @Override
    public List<BillDetail> findAll() {
        return billDetailRepository.findAll();
    }

    @Override
    public List<BillDetail> findByCreateDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        Date startDate = Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
        return billDetailRepository.findByCreateDateBetween(startDate, endDate);
    }

    @Override
    public List<BillDetail> findByCreateDateBetween(LocalDate start, LocalDate end) {
        Date startDate = Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(end.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());
        return billDetailRepository.findByCreateDateBetween(startDate, endDate);
    }

    @Override
    public List<VoucherPriceDTO> getVoucherDetail(String code) {
        // Lấy kết quả từ repository (các giá trị trả về là Object[])
        List<Object[]> results = voucherDetailRepository.findPriceForBillCodeCustomer(code);

        // Chuyển đổi sang VoucherPriceDTO
        List<VoucherPriceDTO> voucherPrices = new ArrayList<>();

        for (Object[] result : results) {
            VoucherPriceDTO dto = new VoucherPriceDTO(
                    (BigDecimal) result[0],  // before_price
                    (BigDecimal) result[1],  // after_price
                    (BigDecimal) result[2],  // discount_price
                    (BigDecimal) result[3]   // money_ship
            );
            voucherPrices.add(dto);
        }

        return voucherPrices;
    }
}



