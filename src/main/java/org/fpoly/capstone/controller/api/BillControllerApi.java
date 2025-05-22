package org.fpoly.capstone.controller.api;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.fpoly.capstone.dto.address.BaseAddressRequest;
import org.fpoly.capstone.dto.bill.*;
import org.fpoly.capstone.dto.vnpay.CreatePayMentMethodRequest;
import org.fpoly.capstone.dto.billDetail.BillProductDTO;
import org.fpoly.capstone.dto.vnpay.PayMentVnPayResponse;
import org.fpoly.capstone.dto.voucher.VoucherRequest;
import org.fpoly.capstone.entity.*;
import org.fpoly.capstone.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.*;

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
        Bill bill = billService.createBillCode(id);
        return bill;
    }

    @GetMapping("/getAllBill")
    public List<Bill> getAllBill() {
        return billService.getBillTaoHoaDon();
    }

    @DeleteMapping("/deleteBill/{id}")
    public String deleteBill(@PathVariable Long id) {
        Bill bill = billService.deleteBill(id);
        return "Delete successful";
    }

    @PostMapping("/save-bill/{id}")
    public Bill saveBill(@PathVariable Long id, @RequestBody CreateBillOfflineDTO request) {
        Bill bill = billService.save(id, request);
        return bill;
    }

    @GetMapping("/products/{billId}")
    public List<BillProductDTO> getProducts(@PathVariable Long billId) {
        return billService.getBillDetail(billId);

    }
    @GetMapping("/search-product-customer/{code}")
    public List<BillProductDTO> getProductsCustomer(@PathVariable String code) {
        return billService.getBillDetailCustomer(code);
    }

    @PostMapping("/save-product-bill/{id}")
    public Bill saveProductBill(@PathVariable Long id, @RequestBody CreateBillOfflineDTO request) {
        Bill bill = billService.saveProductInBill(id, request);
        return bill;
    }

    @DeleteMapping("/delete-product-bill/{idBill}/{idProduct}")
    public Bill deleteProductBill(@PathVariable Long idBill, @PathVariable Long idProduct) {
        Bill bill = billService.deleteProductInBill(idBill, idProduct);
        return bill;
    }

    @GetMapping("/address-user/{idUser}")
    public List<BaseAddressRequest> getAllAddressUser(@PathVariable Long idUser) {
        List<BaseAddressRequest> list = billService.getAllAddressUser(idUser);
        return list;
    }

    @GetMapping("/getMinimumBill")
    public List<VoucherRequest> getMinimumBill(@Param("minimumBill") Integer minimumBill) {
        List<VoucherRequest> voucherRequest = billService.getVoucherMinimumbill(minimumBill);
        return voucherRequest;
    }
    @GetMapping("/getMinimumBillNoLogin")
    public List<VoucherRequest> getMinimumBillNoLogin(@Param("minimumBill") Integer minimumBill) {
        List<VoucherRequest> voucherRequest = billService.getVoucherMiniNoLogin(minimumBill);
        return voucherRequest;
    }

    @GetMapping("/getAllProduct")
    public Page<ProductRequest> listProductDetail(@RequestParam(defaultValue = "0") int page
    , @RequestParam(defaultValue = "5") int size) {
        return billService.findAllProductDetail(page, size);
    }

    @GetMapping("/getAllVoucher")
    public Page<VoucherRequest1> listVoucher(@RequestParam(defaultValue = "0")int page ,
                                             @RequestParam(defaultValue = "5")int size,
                                             @RequestParam Integer totalAmount
    ) {

      return billService.findAllVoucherPage(totalAmount , page, size);

    }

    @PostMapping("/payment-vnpay")
    public ResponseEntity<String> payWithVnPay(@RequestBody CreatePayMentMethodRequest payModel, HttpServletRequest request) {
        try {
            String paymetUrl = paymentMethodService.payWithVnpay(payModel, request);
            return ResponseEntity.ok(paymetUrl);  // Trả về URL thanh toán với mã trạng thái 200 OK
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating payment URL: " + e.getMessage());
        }
    }

    @PostMapping("/vnpay-success")
    public ResponseEntity<String> vnPayCallback(@RequestBody PayMentVnPayResponse response) {
        boolean paymentSuccess = paymentMethodService.paymentSucessFully(response);
        if (paymentSuccess) {
            return ResponseEntity.ok("Thanh toán thành công");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Thanh toán không thành công");
        }
    }

    @PostMapping("/vnpay-success-no-login")
    public ResponseEntity<String> vnPayCallbackNoLogin(@RequestBody PayMentVnPayResponse response) {
        boolean paymentSuccess = paymentMethodService.payMentSucessFullyOnlineNoLogin(response);
        if (paymentSuccess) {
            return ResponseEntity.ok("Thanh toán thành công");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Thanh toán không thành công");
        }
    }

    @PostMapping("/createCustomerBill")
    public ResponseEntity<CreateCustomerBill> createCustomerBill(@RequestBody CreateCustomerBill request) {
        CreateCustomerBill createCustomerBill = billService.createCustomerBill(request);
        return ResponseEntity.ok(createCustomerBill);
    }

    @GetMapping("/customerPage")
    public Page<GetAllCusomter> getCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return billService.findALlCustomerPage(page, size);
    }
    @GetMapping("/searchCustomer")
    public List<GetAllCusomter> searchCustomer( @RequestParam(value = "searchQuery", required = false) String searchQuery
                                                ){
        return billService.searchCustomer(searchQuery);
    }
    @GetMapping("/searchProduct")
    public Page<ProductRequest> getProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) Long color,
            @RequestParam(required = false) Long material,
            @RequestParam(required = false) Long kichCo,
            @RequestParam(required = false) Long brand,
            @RequestParam(defaultValue = "0") int page ,
            @RequestParam(defaultValue = "5") int size) {

        return billService.searchProduct(name, category, color, material, kichCo, brand, page , size);
    }
    @GetMapping("/getAllBrand")
    public List<Brand> getAllBrand() {
        return billService.findAllBrand();
    }
    @GetMapping("/getAllSize")
    public List<Size> getAllSize() {
        return billService.findAllSize();
    }
    @GetMapping("/getAllCategory")
    public List<Category> getAllCategory() {
        return billService.findAllCategory();
    }
    @GetMapping("/getAllMaterial")
    public List<Material> getAllMaterial() {
        return billService.findAllMaterial();
    }
    @GetMapping("/getAllColor")
    public List<Color> getAllColor() {
        return billService.findAllColor();
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchBill(@RequestParam("code") String code) {
        Optional<Bill> billOpt = billService.searchCode(code);
        if (billOpt.isPresent()) {
            return ResponseEntity.ok(billOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mã hóa đơn không tồn tại.");
        }
    }
    @PostMapping("/createBill-Customer")
    public Bill createBill(@RequestBody CreateBillCustomerOnlineRequest request) throws MessagingException {
        Bill bill = billService.createBillOnlieCustomerRequest(request);
        return bill;
    }
    @GetMapping("/detail-productId/{idProduct}/size/{idSize}/color/{idColor}")
    public ResponseEntity<?> getProductDetailById(@PathVariable Long idProduct, @PathVariable Long idSize, @PathVariable Long idColor) {
        Optional<ProductDetail> productDetail = billService.finProductDetailById(idProduct, idSize, idColor);
        if (productDetail.isPresent()) {
            return ResponseEntity.ok(productDetail.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sản phẩm với ID: " + idProduct);
        }
    }
    @GetMapping("/getQuantityProductDetail/{idProductDetail}")
    public ResponseEntity<?> getProductDetailById(@PathVariable Integer idProductDetail) {
        Optional<ProductDetail> productDetail = billService.findByIDProductDetail(idProductDetail);
        if (productDetail.isPresent()) {
            return ResponseEntity.ok(productDetail.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sản phẩm với ID: " + idProductDetail);
        }
    }

}
