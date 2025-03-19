package org.fpoly.capstone.service.impl;

import org.fpoly.capstone.dto.billDetail.ChangeStatusBillRequest;
import org.fpoly.capstone.dto.billDetail.StatusBillDetailRequest;
import org.fpoly.capstone.dto.billDetail.UpdateInForCustomer;
import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.BillDetail;
import org.fpoly.capstone.entity.BillHistory;
import org.fpoly.capstone.entity.ProductDetail;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.enum_status.BillStatus;
import org.fpoly.capstone.entity.enum_status.ProductVariantStatus;
import org.fpoly.capstone.entity.enum_status.UserRole;
import org.fpoly.capstone.repository.BillDetailRepository;
import org.fpoly.capstone.repository.BillHistotyRepository;
import org.fpoly.capstone.repository.BillRepository;
import org.fpoly.capstone.repository.ProductDetailRepository;
import org.fpoly.capstone.repository.UserRepository;
import org.fpoly.capstone.service.BillDetaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BillDetailServiceImpl implements BillDetaiService {
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private BillDetailRepository billDetailRepository;
    @Autowired
    private BillHistotyRepository billHistotyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductDetailRepository productDetailRepository;


    @Override
    public Bill changeStatusBill(Long id, Long idEmployess, ChangeStatusBillRequest request) {
        Optional<Bill> bill = this.billRepository.findById(id);
        Optional<User> user = this.userRepository.findById(idEmployess);
        if (!bill.isPresent()) {
            throw new RuntimeException("Bill not found");
        }
        if (!user.isPresent()) {
            throw new RuntimeException("User not found");
        }
        boolean checkDaThanhToan = this.billHistotyRepository.findAllByBill(bill.get()).stream()
                .anyMatch(invoice -> invoice.getStatus() == BillStatus.DA_THANH_TOAN);
        BillStatus statusBill[] = BillStatus.values();
        int nextIndex = (bill.get().getStatus().ordinal() + 1) % statusBill.length;
        bill.get().setStatus(BillStatus.valueOf(statusBill[nextIndex].name()));
        if (nextIndex > 6) {
            throw new RuntimeException("Bill status is " + statusBill[nextIndex].name());
        }
        if (bill.get().getStatus() == BillStatus.XAC_NHAN) {
            System.out.println("Chay vao xac nhan");
            if (bill.get().getUser() == null) {
                Long id_bill = bill.get().getId();

                List<BillDetail> billDetalOnlineList = this.billDetailRepository.findByBillId(id_bill);

                System.out.println("BillDetails found: " + billDetalOnlineList.size()); // Logging số lượng BillDetail

                if (!billDetalOnlineList.isEmpty()) {
                    for (BillDetail billDetail : billDetalOnlineList) {
                        Long productDetailId = billDetail.getProductDetail().getId();
                        Optional<ProductDetail> productDetail = this.productDetailRepository.findById(productDetailId);

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

                            this.productDetailRepository.save(product);
                            System.out.println("Product saved with updated quantity: " + product.getQuantity());
                        } else {
                            System.out.println("Product not found for ID: " + productDetailId); // Logging khi không tìm thấy sản phẩm
                        }
                    }
                } else {
                    System.out.println("No BillDetail found for Bill ID: " + id_bill); // Logging khi không có BillDetail
                }

            } else {
                System.out.println("User is already assigned to the bill: " + bill.get().getUser().getFullName());
            }

            // Cập nhật ngày hoàn thành
            bill.get().setCompletionDate(LocalDateTime.now());
            System.out.println("Bill status updated to XAC_NHAN, Completion date set.");
        } else if (bill.get().getStatus() == BillStatus.DA_THANH_TOAN) {
            bill.get().setReceiveDate(Calendar.getInstance().getTime());
            if (checkDaThanhToan) {
                bill.get().setStatus(BillStatus.THANH_CONG);
                bill.get().setCompletionDate(this.getCurrentTimestampInVietnam());
            }
        }
        bill.get().setLastModifiedDate(LocalDateTime.now());
        bill.get().setEmployee(user.get());
        BillHistory billHistory = new BillHistory();
        billHistory.setBill(bill.get());
        billHistory.setStatus(bill.get().getStatus());
        billHistory.setActionDescription(request.getActionDescription());
        billHistory.setUser(user.get());
        this.billHistotyRepository.save(billHistory);
        Bill billResponse = this.billRepository.save(bill.get());
        return billResponse;
    }

    @Override
    public List<StatusBillDetailRequest> getStatusBillHistory(Long id) {
        List<Object[]> results = this.billHistotyRepository.findAllStatusExcludingTaoHoaDon(id);
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
        return this.billRepository.findById(id).get();
    }

    @Override
    public Bill updateInforBill(Long id, UpdateInForCustomer request) {
        System.out.println("Check vào đây");
        Optional<Bill> optionalBill = this.billRepository.findById(id);
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
        this.billRepository.save(bill);
        return bill;
    }

    @Override
    public Bill cancelBillAdmin(Long id, Long idEmployess, ChangeStatusBillRequest request) {
        Optional<Bill> bill = this.billRepository.findById(id);
        Optional<User> user = this.userRepository.findById(idEmployess);
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
            if (bill.get().getUser() == null) {
                Long idBill = bill.get().getId();
                List<BillDetail> billDetailOnlineList = this.billDetailRepository.findByBillId(idBill);
                if (!billDetailOnlineList.isEmpty()) {
                    for (BillDetail billDetail : billDetailOnlineList) {
                        Long productDetailId = billDetail.getProductDetail().getId();
                        Optional<ProductDetail> productDetail = this.productDetailRepository.findById(productDetailId);
                        if (productDetail.isPresent()) {
                            ProductDetail product = productDetail.get();
                            product.setQuantity(product.getQuantity() + billDetail.getQuantity());

                            if (product.getStatus() == ProductVariantStatus.HET_SAN_PHAM) {
                                product.setStatus(ProductVariantStatus.DANG_SU_DUNG);
                            }
                            this.productDetailRepository.save(product);
                        }
                    }
                }
            }
            bill.get().setLastModifiedDate(LocalDateTime.now());
            bill.get().setStatus(BillStatus.DA_HUY);
            bill.get().setEmployee(user.get());
            BillHistory billHistory = new BillHistory();
            billHistory.setBill(bill.get());
            billHistory.setStatus(bill.get().getStatus());
            billHistory.setActionDescription(request.getActionDescription());
            billHistory.setUser(user.get());
            this.billHistotyRepository.save(billHistory);
            return this.billRepository.save(bill.get());

        }
        return bill.get();
    }


    private LocalDateTime getCurrentTimestampInVietnam() {
        Instant instant = Instant.now();
        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        return instant.atZone(zoneId).toLocalDateTime();
    }

    public boolean confirmPayment(Long billId, String note) {
        Optional<Bill> billOpt = this.billRepository.findById(billId);
        if (billOpt.isPresent()) {
            Bill bill = billOpt.get();
            BillStatus oldStatus = bill.getStatus();
            BillStatus newStatus;

            switch (oldStatus) {
                case CHO_XAC_NHAN:
                    newStatus = BillStatus.XAC_NHAN;
                    break;
                case XAC_NHAN:
                    newStatus = BillStatus.CHO_VAN_CHUYEN;
                    break;
                case CHO_VAN_CHUYEN:
                    newStatus = BillStatus.VAN_CHUYEN;
                    break;
                case VAN_CHUYEN:
                    newStatus = BillStatus.DA_THANH_TOAN;
                    break;
                case DA_THANH_TOAN:
                    newStatus = BillStatus.THANH_CONG;
                    break;
                default:
                    return false;
            }

            bill.setStatus(newStatus);
            this.billRepository.save(bill);
            this.saveBillHistory(bill, oldStatus, newStatus, note); // Gửi note vào đây
            return true;
        }
        return false;
    }

    private String convertStatus(BillStatus status) {
        if (status == null) {
            return "Không xác định";
        }
        switch (status) {
            case CHO_XAC_NHAN:
                return "Chờ xác nhận";
            case CHO_VAN_CHUYEN:
                return "Chờ vận chuyển";
            case VAN_CHUYEN:
                return "Đang vận chuyển";
            case XAC_NHAN:
                return "Đã xác nhận";
            case DA_THANH_TOAN:
                return "Đã thanh toán";
            case THANH_CONG:
                return "Hoàn thành";
            case TRA_HANG:
                return "Trả hàng";
            case DA_HUY:
                return "Hủy";
            default:
                return "Không xác định";
        }
    }

    private void saveBillHistory(Bill bill, BillStatus oldStatus, BillStatus newStatus, String note) {
        BillHistory history = new BillHistory();
        history.setBill(bill);
        history.setStatus(newStatus);

        if (note != null && !note.trim().isEmpty()) {
            history.setActionDescription(note.trim());
        } else {
            String oldStatusVN = this.convertStatus(oldStatus);
            String newStatusVN = this.convertStatus(newStatus);
            history.setActionDescription("Chuyển trạng thái từ " + oldStatusVN + " sang " + newStatusVN);
        }


        history.setCreateDate(new Date());
        history.setLastModifiedDate(new Date());

        this.billHistotyRepository.save(history);
    }
}

