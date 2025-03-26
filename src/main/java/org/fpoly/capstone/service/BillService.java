package org.fpoly.capstone.service;

import org.fpoly.capstone.dto.address.BaseAddressRequest;
import org.fpoly.capstone.dto.bill.*;
import org.fpoly.capstone.dto.billDetail.BillProductDTO;
import org.fpoly.capstone.dto.voucher.VoucherRequest;
import org.fpoly.capstone.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.Cart;
import org.fpoly.capstone.service.payload.bill.CreateBillRequest;

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

    Bill findById(Long id);

    void saveToBillForOnlineUser(Cart cart, CreateBillRequest request);

    List<Bill> findBillsByCustomerId(Long customerId);

}
