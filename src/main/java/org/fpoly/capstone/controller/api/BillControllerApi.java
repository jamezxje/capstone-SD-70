package org.fpoly.capstone.controller.api;

import jakarta.servlet.http.HttpServletRequest;
import org.fpoly.capstone.dto.address.BaseAddressRequest;
import org.fpoly.capstone.dto.bill.CreateBillOfflineDTO;
import org.fpoly.capstone.dto.bill.CreateCustomerBill;
import org.fpoly.capstone.dto.bill.GetAllCusomter;
import org.fpoly.capstone.dto.bill.ProductRequest;
import org.fpoly.capstone.dto.bill.VoucherRequest1;
import org.fpoly.capstone.dto.billDetail.BillProductDTO;
import org.fpoly.capstone.dto.vnpay.CreatePayMentMethodRequest;
import org.fpoly.capstone.dto.vnpay.PayMentVnPayResponse;
import org.fpoly.capstone.dto.voucher.VoucherRequest;
import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.service.BillService;
import org.fpoly.capstone.service.CustomerService;
import org.fpoly.capstone.service.EmailService;
import org.fpoly.capstone.service.PaymentMethodService;
import org.fpoly.capstone.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
public class BillControllerApi {
    @Autowired
    private BillService billService;
    @Autowired
    private ProductDetailService productDetailService;
    @Autowired
    private PaymentMethodService paymentMethodService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/sale-counter/{id}")
    public Bill createCodeBill(@PathVariable Long id) {
        Bill bill = this.billService.createBillCode(id);
        return bill;
    }

    @GetMapping("/getAllBill")
    public List<Bill> getAllBill() {
        List<Bill> list = this.billService.getBillTaoHoaDon();
        return list;
    }

    @DeleteMapping("/deleteBill/{id}")
    public String deleteBill(@PathVariable Long id) {
        Bill bill = this.billService.deleteBill(id);
        return "Delete successful";
    }

    @PostMapping("/save-bill/{id}")
    public Bill saveBill(@PathVariable Long id, @RequestBody CreateBillOfflineDTO request) {
        Bill bill = this.billService.save(id, request);
        return bill;
    }

    @GetMapping("/products/{billId}")
    public List<BillProductDTO> getProducts(@PathVariable Long billId) {
        return this.billService.getBillDetail(billId);

    }

    @PostMapping("/save-product-bill/{id}")
    public Bill saveProductBill(@PathVariable Long id, @RequestBody CreateBillOfflineDTO request) {
        Bill bill = this.billService.saveProductInBill(id, request);
        return bill;
    }

    @DeleteMapping("/delete-product-bill/{idBill}/{idProduct}")
    public Bill deleteProductBill(@PathVariable Long idBill, @PathVariable Long idProduct) {
        Bill bill = this.billService.deleteProductInBill(idBill, idProduct);
        return bill;
    }

    @GetMapping("/address-user/{idUser}")
    public List<BaseAddressRequest> getAllAddressUser(@PathVariable Long idUser) {
        List<BaseAddressRequest> list = this.billService.getAllAddressUser(idUser);
        return list;
    }

    @GetMapping("/getMinimumBill")
    public List<VoucherRequest> getMinimumBill(@Param("minimumBill") Integer minimumBill) {
        List<VoucherRequest> voucherRequest = this.billService.getVoucherMinimumbill(minimumBill);
        return voucherRequest;
    }

    @GetMapping("/getAllProduct")
    public Page<ProductRequest> listProductDetail(@RequestParam(defaultValue = "0") int page
            , @RequestParam(defaultValue = "5") int size) {
        return this.billService.findAllProductDetail(page, size);
    }

    @GetMapping("/getAllVoucher")
    public Page<VoucherRequest1> listVoucher(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "5") int size,
                                             @RequestParam Integer totalAmount
    ) {
        return this.billService.findAllVoucherPage(totalAmount, page, size);

    }

    @PostMapping("/payment-vnpay")
    public ResponseEntity<String> payWithVnPay(@RequestBody CreatePayMentMethodRequest payModel, HttpServletRequest request) {
        try {
            String paymetUrl = this.paymentMethodService.payWithVnpay(payModel, request);
            return ResponseEntity.ok(paymetUrl);  // Trả về URL thanh toán với mã trạng thái 200 OK
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating payment URL: " + e.getMessage());
        }
    }

    @PostMapping("/vnpay-success")
    public ResponseEntity<String> vnPayCallback(@RequestBody PayMentVnPayResponse response) {
        boolean paymentSuccess = this.paymentMethodService.paymentSucessFully(response);
        if (paymentSuccess) {
            return ResponseEntity.ok("Thanh toán thành công");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Thanh toán không thành công");
        }
    }

    @PostMapping("/createCustomerBill")
    public ResponseEntity<CreateCustomerBill> createCustomerBill(@RequestBody CreateCustomerBill request) {
        CreateCustomerBill createCustomerBill = this.billService.createCustomerBill(request);
        return ResponseEntity.ok(createCustomerBill);
    }

    @GetMapping("/customerPage")
    public Page<GetAllCusomter> getCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return this.billService.findALlCustomerPage(page, size);
    }

    @GetMapping("/searchCustomer")
    public List<GetAllCusomter> searchCustomer(@RequestParam(value = "searchQuery", required = false) String searchQuery
    ) {
        return this.billService.searchCustomer(searchQuery);
    }


}
