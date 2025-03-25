package org.fpoly.capstone.service.impl;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.fpoly.capstone.constant.MessageError;
import org.fpoly.capstone.dto.address.BaseAddressRequest;
import org.fpoly.capstone.dto.bill.*;
import org.fpoly.capstone.dto.billDetail.BillProductDTO;
import org.fpoly.capstone.dto.voucher.VoucherRequest;
import org.fpoly.capstone.entity.*;
import org.fpoly.capstone.entity.enum_status.*;
import org.fpoly.capstone.repository.*;
import org.fpoly.capstone.service.BillService;
import org.fpoly.capstone.service.EmailService;
import org.fpoly.capstone.service.VoucherService;
import org.fpoly.capstone.utils.general.GeneralStringCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;
    @Autowired
    private GeneralStringCode generalStringCode;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BillHistoryRepository billHistoryRepository;
    @Autowired
    private BillDetailRepository billDetailRepository;
    @Autowired
    private ProductDetailRepository productDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private VoucherDetailReponsitory voucherDetailReponsitory;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ColorRepository colorRepository;
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private EmailService emailService;
    @Override
    public CreateBillRequest create(CreateBillRequest createBillDTO) {
        return new CreateBillRequest();
    }


    @Override
    public Bill createBillCode(Long idEmployee) {
        Optional<User> employee = userRepository.findById(idEmployee);
        if (!employee.isPresent()) {
            throw new RuntimeException("Employee not found");
        }
        Bill bill = Bill.builder()
                .code(generalStringCode.generateCodeAdmin())
                .employee(employee.get())
                .userName("")
                .note("")
                .type(BillType.OFFLINE)
                .status(BillStatus.TAO_HOA_DON)
                .address("")
                .phoneNumber("")
                .email("")
                .itemDiscount(new BigDecimal("0"))
                .totalMoney(new BigDecimal("0"))
                .moneyShip(new BigDecimal("0"))
                .build();
        billRepository.save(bill);
        billHistoryRepository.save(BillHistory.builder()
                .status(bill.getStatus())
                .bill(bill)
                .user(bill.getUser())
                .build());
        return bill;
    }

    @Override
    public List<Bill> getBillTaoHoaDon() {
        return billRepository.getBillTAOHOADON();
    }

    @Transactional
    @Override
    public Bill deleteBill(Long id) {
        Optional<Bill> findIdBill = billRepository.findById(id);
        if (findIdBill.isPresent()) {
            Bill bill = findIdBill.get();
            billHistoryRepository.deleteAllByBillId(bill.getId());
            billRepository.delete(bill);
            return bill;
        } else {
            throw new RuntimeException("Bill not found");
        }
    }

    @Override
    public Bill save(Long id, CreateBillOfflineDTO request) {
        Optional<Bill> findIdBill = billRepository.findById(id);
        if (!findIdBill.isPresent()) {
            throw new RuntimeException("Bill not found");
        }
        Optional<User> user = userRepository.findById(request.getIdUser());
        if (!user.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user1 = user.get();
        Bill bill = findIdBill.get();
        bill.setUser(user1);
        bill.setUserName(request.getUserName());
        bill.setNote(request.getNote());
        bill.setAddress(request.getAddress());
        bill.setPhoneNumber(request.getPhoneNumber());
        bill.setEmail(request.getEmail());
        bill.setItemDiscount(new BigDecimal(request.getItemDiscount()));
        bill.setTotalMoney(new BigDecimal(request.getTotalMoney()));
        bill.setMoneyShip(new BigDecimal(request.getMoneyShip()));
        bill.setLastModifiedDate(Calendar.getInstance().getTime());
        if (request.getDeliveryDate() != null) {
            bill.setShipDate(request.getDeliveryDate());
        }
        System.out.println("Check bill" + request.getType());
        if (!request.isOpenDelivery()) {
            bill.setStatus(BillStatus.THANH_CONG);
            bill.setCompletionDate(getCurrmentTimeStampInVN());
            billRepository.save(bill);
            billHistoryRepository.save(BillHistory.builder()
                    .status(BillStatus.THANH_CONG)
                    .bill(bill)
                    .user(bill.getEmployee())
                    .build());
            System.out.println("Check vô đây");
        } else {
            bill.setStatus(BillStatus.CHO_XAC_NHAN);
            bill.setCompletionDate(getCurrmentTimeStampInVN());
            billRepository.save(bill);
            billHistoryRepository.save(BillHistory.builder()
                    .status(BillStatus.CHO_XAC_NHAN)
                    .bill(bill)
                    .user(bill.getEmployee())
                    .build());
            System.out.println("Check vào day");
        }

        request.getVoucherDetails().forEach(voucher -> {
            System.out.println("Processing voucher with ID: " + voucher.getIdVoucher());
            Optional<Voucher> vouchers = voucherRepository.findById(voucher.getIdVoucher());
            System.out.println("Check voucher" + voucher.getIdVoucher());
            if (!vouchers.isPresent()) {
                throw new RuntimeException("Voucher not found");
            }
            if (vouchers.get().getQuantity() <= 0 && vouchers.get().getEndDate().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Voucher end date is less than current date");
            }
            vouchers.get().setQuantity(vouchers.get().getQuantity() - 1);
            voucherRepository.save(vouchers.get());
            VoucherDetail voucherDetail = VoucherDetail.builder()
                    .voucher(vouchers.get())
                    .bill(bill)
                    .afterPrice(new BigDecimal(voucher.getAfterVoucher()))
                    .beforePrice(new BigDecimal(voucher.getBeforVoucher()))
                    .discountPrice(new BigDecimal(voucher.getDiscountVoucher()))
                    .build();
            voucherDetailReponsitory.save(voucherDetail);
        });
        try {
            if (bill.getEmail() != null) {
                sendInVoiceEmail(bill);
            }else{
                System.out.println("No send Mail is email Null ");
            }

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return bill;
    }

    @Override
    public Bill saveProductInBill(Long id, CreateBillOfflineDTO request) {

        if (request.getBillDetails() == null) {
            throw new RuntimeException("Bill details are required");
        }
        Optional<Bill> findIdBill = billRepository.findById(id);
        if (!findIdBill.isPresent()) {
            throw new RuntimeException("Bill not found");
        }
        Bill bill = findIdBill.get();
        request.getBillDetails().forEach(detail -> {
            Optional<ProductDetail> productDetail = productDetailRepository.findById(Long.valueOf(detail.getIdProduct()));
            if (!productDetail.isPresent()) {
                throw new RuntimeException("Product detail not found");
            }
            if (productDetail.get().getQuantity() < detail.getQuantity()) {
                throw new RuntimeException("Product detail quantity exceeds quantity");
            }
            if (productDetail.get().getStatus() != ProductVariantStatus.DANG_SU_DUNG) {
                throw new RuntimeException("Product detail status not DANG_SU_DUNG");
            }
            Optional<BillDetail> detailBill = billDetailRepository.findByBillAndProductDetail(bill, productDetail.get());
            if (detailBill.isPresent()) {

                BillDetail billDetailToUpdate = detailBill.get();
                int newQuantity = billDetailToUpdate.getQuantity() + detail.getQuantity();
                billDetailToUpdate.setQuantity(newQuantity); // Cập nhật số lượng mới
                billDetailRepository.save(billDetailToUpdate);
                productDetail.get().setQuantity(productDetail.get().getQuantity() - detail.getQuantity());
                if (productDetail.get().getQuantity() == 0) {
                    productDetail.get().setStatus(ProductVariantStatus.HET_SAN_PHAM);
                }
                productDetailRepository.save(productDetail.get());
                System.out.println("hàm 1");
            } else {
                System.out.println("Ham 2");
                BillDetail billDetail = BillDetail.builder()
                        .statusBill(BillStatus.THANH_CONG)
                        .bill(findIdBill.get())
                        .productDetail(productDetail.get())
                        .price(productDetail.get().getPrice())
                        .quantity(detail.getQuantity())
                        .build();
                billDetailRepository.save(billDetail);
                productDetail.get().setQuantity(productDetail.get().getQuantity() - billDetail.getQuantity());
                if (productDetail.get().getQuantity() == 0) {
                    productDetail.get().setStatus(ProductVariantStatus.HET_SAN_PHAM);
                }
                productDetailRepository.save(productDetail.get());
                bill.setMethod(PaymentMethod.TIEN_MAT);
                billRepository.save(bill);
            }
        });

        return bill;
    }

    @Override
    @Transactional
    public Bill deleteProductInBill(Long idBill, Long idProduct) {
        Optional<Bill> optional = billRepository.findById(idBill);
        if (!optional.isPresent()) {
            throw new RuntimeException("Bill not found");
        }
        List<BillDetail> listBillDetail = billDetailRepository.findByBillId(optional.get().getId());
        listBillDetail.forEach(item -> {
            Optional<ProductDetail> productDetail = productDetailRepository.findById(Long.valueOf(item.getProductDetail().getId()));
            if (!productDetail.isPresent()) {
                throw new RuntimeException("Product detail not found");
            }
            productDetail.get().setQuantity(productDetail.get().getQuantity() + item.getQuantity());
            if (productDetail.get().getStatus() == ProductVariantStatus.HET_SAN_PHAM) {
                productDetail.get().setStatus(ProductVariantStatus.DANG_SU_DUNG);
            }
            productDetailRepository.save(productDetail.get());
            billHistoryRepository.deleteAllByBillId(optional.get().getId());
            billDetailRepository.deleteByProductDetailId(idProduct);
        });
        return optional.get();
    }

    @Override
    public List<BaseAddressRequest> getAllAddressUser(Long idUser) {
        List<Object[]> listAddress = addressRepository.findByUserId(idUser);
        for (Object[] result : listAddress) {
            for (int i = 0; i < result.length; i++) {
                System.out.println("Index " + i + ": " + result[i] + " (type: " + result[i].getClass().getName() + ")");
            }
        }

        List<BaseAddressRequest> baseAddressRequests = new ArrayList<>();
       for (Object[] list : listAddress) {
           Long id = (Long) list[0];
           String line = (String) list[1];
           String district = (String) list[2];
           String province = (String) list[3];
           String ward = (String) list[4];
           Integer districtId = (Integer) list[5];
           Integer provinceId = (Integer) list[6];
           String wardCode = (String) list[7];
           String fullName = (String) list[8];
           String phoneNumber = (String) list[9];
          AddressStatus status = (AddressStatus) list[10];
           Long userId = (Long) list[11];
           BaseAddressRequest baseAddressRequest = new BaseAddressRequest(
                   id , line , district , province , ward , districtId , provinceId , wardCode , fullName , phoneNumber ,status , userId
           );
           baseAddressRequests.add(baseAddressRequest);
       }
       return baseAddressRequests;
    }

    @Override
    public List<VoucherRequest> getVoucherMinimumbill(Integer minimumBill) {
        System.out.println("Check dữ liệu" + voucherRepository.getVoucherMinimumBill(minimumBill));
        List<Object[]> list = voucherRepository.getVoucherMinimumBill(minimumBill);
       List<VoucherRequest> voucherRequests = new ArrayList<>();
       for (Object[] result : list) {
           Long id = (Long) result[0];
           String code = (String) result[1];
           BigDecimal value = (BigDecimal) result[3];
           String name = (String) result[2];
           Integer minimumbill = (Integer) result[4];
           Integer quantity = (Integer) result[5];
           LocalDateTime startDate = (LocalDateTime) result[6];
           LocalDateTime endDate = (LocalDateTime) result[7];
           VoucherStatus voucherStatus = (VoucherStatus) result[8];
           VoucherRequest voucherRequest = new VoucherRequest(id , code , name , value,
                   minimumbill , quantity, startDate , endDate , voucherStatus);
           voucherRequests.add(voucherRequest);
       }
       return voucherRequests;
    }

    @Override
    public Page<ProductRequest> findAllProductDetail(int page , int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> list = productRepository.findAllProductDetails(pageable);
        List<ProductRequest> productRequests = new ArrayList<>();
        for (Object[] result : list) {
            Long id = (Long) result[0];
            String code = (String) result[1];
            String name = (String) result[2];
            String categoryName = (String) result[3];
            String sizeName = (String) result[4];
            String colorName = (String) result[5];
            String materialName = (String) result[6];
            String brandName = (String) result[7];
            Integer quantity = (Integer) result[8];
            BigDecimal price = (BigDecimal) result[9];
            Gender gender = (Gender) result[10];
            ProductVariantStatus status = (ProductVariantStatus) result[11];
            ProductRequest productRequest = new ProductRequest(id , code , name , categoryName , sizeName , colorName ,
                    materialName , brandName , quantity , price , gender , status) ;
            productRequests.add(productRequest);
        }
       return new PageImpl<>(productRequests, pageable, list.getTotalElements());
    }

    @Override
    public List<Voucher> getAllVoucher() {
        return voucherRepository.findAll();
    }

    @Override
    public CreateCustomerBill createCustomerBill(CreateCustomerBill createCustomerBill) {
        if (createCustomerBill.getCustomerName() == null || createCustomerBill.getCustomerName().isEmpty()) {
            throw new IllegalArgumentException(MessageError.NAME_NULL.getMessage());
        }
        if(createCustomerBill.getCustomerPhone() == null || createCustomerBill.getCustomerPhone().isEmpty()) {
            throw new IllegalArgumentException(MessageError.PHONE_NULL.getMessage());
        }
        if (createCustomerBill.getCustomerEmail() == null || createCustomerBill.getCustomerEmail().isEmpty()) {
            throw new IllegalArgumentException(MessageError.EMAIL_NULL.getMessage());
        }
       if (customerRepository.existsByPhoneNumber(createCustomerBill.getCustomerPhone())){
           throw new IllegalArgumentException(MessageError.PHONE_ERROR.getMessage());
       }
       if (customerRepository.existsByEmail(createCustomerBill.getCustomerEmail())){
           throw new IllegalArgumentException(MessageError.EMAIL_ERROR.getMessage());
       }
       User user = User.builder()
               .fullName(createCustomerBill.getCustomerName())
               .phoneNumber(createCustomerBill.getCustomerPhone())
               .email(createCustomerBill.getCustomerEmail())
               .roles(UserRole.ROLE_CUSTOMER)
               .status(UserStatus.ACTIVATED)
               .build();
       userRepository.save(user);
       return createCustomerBill;
    }

    @Override
    public Page<GetAllCusomter> findALlCustomerPage(int page , int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> results = customerRepository.findAllCustomerPage(pageable);
        List<GetAllCusomter> cusomters = new ArrayList<>();
        for (Object[] result : results) {
            Long id = (Long) result[0];
            String fullName = (String) result[1];
            String phoneNumber = (String) result[2];
            String email = (String) result[3];
            Date lastModifiedDate = (Date) result[4];
            GetAllCusomter getAllCusomter = new GetAllCusomter(id, fullName, phoneNumber, email, lastModifiedDate);
            cusomters.add(getAllCusomter);
        }
        return new PageImpl<>(cusomters, pageable, results.getTotalElements());

    }

    @Override
    public Page<VoucherRequest1> findAllVoucherPage(Integer totalAmount , int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> results = voucherRepository.findAllVoucherRequests(totalAmount , pageable);
        List<VoucherRequest1> voucherRequests = new ArrayList<>();
        for (Object[] result : results ){
            Long id = (Long) result[0];
            String code = (String) result[1];
            String name = (String) result[2];
            BigDecimal value = (BigDecimal) result[3];
            Integer minimumbill = (Integer) result[4];
            Integer quantity = (Integer) result[5];
            LocalDateTime startDate = (LocalDateTime) result[6];
            LocalDateTime endDate = (LocalDateTime) result[7];
            VoucherRequest1 voucherRequest = new VoucherRequest1(id , code , name , value , minimumbill , quantity , startDate , endDate);
            voucherRequests.add(voucherRequest);
        }
       return new PageImpl<>(voucherRequests , pageable , results.getTotalElements());
    }

    @Override
    public List<GetAllCusomter> searchCustomer(String searchQuery) {
        List<Object[]> results = customerRepository.searchBySearchQuery(searchQuery);
        List<GetAllCusomter> cusomters = new ArrayList<>();
        for (Object[] result : results) {
            Long id = (Long) result[0];
            String fullName = (String) result[1];
            String phoneNumber = (String) result[2];
            String email = (String) result[3];
            Date lastModifiedDate = (Date) result[4];
            GetAllCusomter getAllCusomter = new GetAllCusomter(id, fullName, phoneNumber, email, lastModifiedDate);
            cusomters.add(getAllCusomter);
        }
        return cusomters;
    }

    @Override
    public Page<ProductRequest> searchProduct(String nameProduct , Long category , Long color , Long material , Long kichCo , Long brand , int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> results = productRepository.findAllProductDetailsBySearch(nameProduct, category, color, material, kichCo, brand, pageable);
        List<ProductRequest> productRequests = new ArrayList<>();
        for (Object[] result : results) {
            Long id = (Long) result[0];
            String code = (String) result[1];
            String name = (String) result[2];
            String categoryName = (String) result[3];
            String sizeName = (String) result[4];
            String colorName = (String) result[5];
            String materialName = (String) result[6];
            String brandName = (String) result[7];
            Integer quantity = (Integer) result[8];
            BigDecimal price = (BigDecimal) result[9];
            Gender gender = (Gender) result[10];
            ProductVariantStatus status = (ProductVariantStatus) result[11];
            ProductRequest productRequest = new ProductRequest(id , code , name , categoryName , sizeName , colorName ,
                    materialName , brandName , quantity , price , gender , status) ;
            productRequests.add(productRequest);
        }
        return new PageImpl<>(productRequests , pageable , results.getTotalElements());
    }

    @Override
    public List<Brand> findAllBrand() {
        return brandRepository.findAll();
    }

    @Override
    public List<Size> findAllSize() {
        return sizeRepository.findAll();
    }

    @Override
    public List<Category> findAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Material> findAllMaterial() {
        return materialRepository.findAll();
    }

    @Override
    public List<Color> findAllColor() {
        return colorRepository.findAll();
    }

    @Override
    public List<BillProductDTO> getBillDetail(Long billId) {
        List<Object[]> results = billDetailRepository.getProductByIDBill(billId);
        List<BillProductDTO> billDetails = new ArrayList<>();
        for (Object[] result : results) {
            Long id = (Long) result[0];
            String name = (String) result[1];
            BigDecimal price = (BigDecimal) result[2];
            Integer quantity = (Integer) result[3];
            String size = (String) result[4];
            String color = (String) result[5];
            Long idProductDetail = (Long) result[6];
            BillProductDTO billProductDTO = new BillProductDTO(id, name, price, quantity, size, color , idProductDetail);
            billDetails.add(billProductDTO);
        }
        return billDetails;
    }


    private Date getCurrmentTimeStampInVN() {
        Instant instant = Instant.now();
        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        return Date.from(instant.atZone(zoneId).toInstant());
    }
    public void sendInVoiceEmail(Bill bill) throws MessagingException {
        String subject = "Hóa đơn thanh toán CAPSTONE";
        String reciprient = bill.getEmail();
        String htmlContent = emailService.generateHtmlContent(bill);
        System.out.println("Gửi email đến: " + reciprient);
        System.out.println("Chủ đề email: " + subject);
        System.out.println("Nội dung email: " + htmlContent);
        emailService.sendEmail(reciprient , subject , htmlContent);
    }
}
