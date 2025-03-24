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

    @Autowired
    private BillHistotyRepository billHistotyRepository;

    @Autowired
    private UserRepository userRepository;

    // Phương thức lấy danh sách hóa đơn có phân trang
    public Page<Bill> getAllBills(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size); // Page index bắt đầu từ 0
        return billRepository.findAll(pageable);
    }

    public boolean confirmPayment(Long billId, String note) {
        Optional<Bill> billOpt = billRepository.findById(billId);
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
            billRepository.save(bill);
            saveBillHistory(bill, oldStatus, newStatus, note); // Gửi note vào đây
            return true;
        }
        return false;
    }

    private String convertStatus(BillStatus status) {
        if (status == null) return "Không xác định";
        switch (status) {
            case CHO_XAC_NHAN: return "Chờ xác nhận";
            case CHO_VAN_CHUYEN: return "Chờ vận chuyển";
            case VAN_CHUYEN: return "Đang vận chuyển";
            case XAC_NHAN: return "Đã xác nhận";
            case DA_THANH_TOAN: return "Đã thanh toán";
            case THANH_CONG: return "Hoàn thành";
            case TRA_HANG: return "Trả hàng";
            case DA_HUY: return "Hủy";
            default: return "Không xác định";
        }
    }

    private void saveBillHistory(Bill bill, BillStatus oldStatus, BillStatus newStatus, String note) {
        BillHistory history = new BillHistory();
        history.setBill(bill);
        history.setStatus(newStatus);

        if (note != null && !note.trim().isEmpty()) {
            history.setActionDescription(note.trim());
        } else {
            String oldStatusVN = convertStatus(oldStatus);
            String newStatusVN = convertStatus(newStatus);
            history.setActionDescription("Chuyển trạng thái từ " + oldStatusVN + " sang " + newStatusVN);
        }


        history.setCreateDate(new Date());
        history.setLastModifiedDate(new Date());

        billHistotyRepository.save(history);
    }

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
