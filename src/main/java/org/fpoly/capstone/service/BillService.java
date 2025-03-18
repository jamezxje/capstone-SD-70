package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.BillHistory;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.enum_status.BillStatus;
import org.fpoly.capstone.repository.BillHistotyRepository;
import org.fpoly.capstone.repository.BillRepository;
import org.fpoly.capstone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillHistotyRepository billHistotyRepository;

    @Autowired
    private UserRepository userRepository;

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
}
