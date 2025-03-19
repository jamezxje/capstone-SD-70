package org.fpoly.capstone.service;

import org.fpoly.capstone.dto.BillDetailDTO;
import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.enum_status.BillStatus;
import org.fpoly.capstone.repository.BillDetailRepository;
import org.fpoly.capstone.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class BillDetailService {
    private static final Logger LOGGER = Logger.getLogger(BillDetailService.class.getName());

    @Autowired
    private BillDetailRepository billDetailRepository;

    @Autowired
    private BillRepository billRepository;

    public List<BillDetailDTO> getBillDetails(Long billId) {
        List<Object[]> results = billDetailRepository.getProductByBillId(billId);
        if (results == null || results.isEmpty()) {
            LOGGER.warning("Không tìm thấy dữ liệu trong cơ sở dữ liệu cho billId: " + billId);
            return new ArrayList<>();
        }

        List<BillDetailDTO> billDetails = new ArrayList<>();
        for (Object[] row : results) {
            billDetails.add(new BillDetailDTO(row));
        }

        return billDetails;
    }

    public boolean confirmPayment(Long billId) {
        Optional<Bill> billOpt = billRepository.findById(billId);
        if (billOpt.isPresent()) {
            Bill bill = billOpt.get();
            switch (bill.getStatus()) {
                case CHO_XAC_NHAN:
                    bill.setStatus(BillStatus.XAC_NHAN);
                    break;
                case XAC_NHAN:
                    bill.setStatus(BillStatus.CHO_VAN_CHUYEN);
                    break;
                case CHO_VAN_CHUYEN:
                    bill.setStatus(BillStatus.VAN_CHUYEN);
                    break;
                case VAN_CHUYEN:
                    bill.setStatus(BillStatus.DA_THANH_TOAN);
                    break;
                default:
                    return false;
            }
            billRepository.save(bill);
            return true;
        }
        return false;
    }
}

