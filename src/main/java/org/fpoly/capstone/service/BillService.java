package org.fpoly.capstone.service;

import org.fpoly.capstone.dto.address.BaseAddressRequest;
import org.fpoly.capstone.dto.bill.*;
import org.fpoly.capstone.dto.billDetail.BillProductDTO;
import org.fpoly.capstone.dto.voucher.VoucherRequest;
import org.fpoly.capstone.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BillService {
    CreateBillRequest create(CreateBillRequest createBillDTO);

    Bill createBillCode(Long idEmployee);

    List<Bill> getBillTaoHoaDon();

    Bill deleteBill(Long id);

    Bill save(Long id, CreateBillOfflineDTO request);

    List<BillProductDTO> getBillDetail(Long billId);

    Bill saveProductInBill(Long id, CreateBillOfflineDTO request);

    Bill deleteProductInBill(Long idBill, Long idProduct);

    List<BaseAddressRequest> getAllAddressUser(Long idUser);

    List<VoucherRequest> getVoucherMinimumbill(Integer minimumBill);

    Page<ProductRequest> findAllProductDetail(int page , int size);

    List<Voucher> getAllVoucher();

    CreateCustomerBill createCustomerBill(CreateCustomerBill createCustomerBill);

    Page<GetAllCusomter> findALlCustomerPage(int page , int size);

    Page<VoucherRequest1> findAllVoucherPage(Integer totalAmount , int page , int size);

    List<GetAllCusomter> searchCustomer(String searchQuery);
}
