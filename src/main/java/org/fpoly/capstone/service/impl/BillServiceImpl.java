package org.fpoly.capstone.service.impl;


import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fpoly.capstone.constant.MessageError;
import org.fpoly.capstone.dto.address.BaseAddressRequest;
import org.fpoly.capstone.dto.bill.CreateBillOfflineDTO;
import org.fpoly.capstone.dto.bill.CreateCustomerBill;
import org.fpoly.capstone.dto.bill.GetAllCusomter;
import org.fpoly.capstone.dto.bill.ProductRequest;
import org.fpoly.capstone.dto.bill.VoucherRequest1;
import org.fpoly.capstone.dto.billDetail.BillProductDTO;
import org.fpoly.capstone.dto.voucher.VoucherRequest;
import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.BillDetail;
import org.fpoly.capstone.entity.BillHistory;
import org.fpoly.capstone.entity.Brand;
import org.fpoly.capstone.entity.Cart;
import org.fpoly.capstone.entity.CartDetail;
import org.fpoly.capstone.entity.Category;
import org.fpoly.capstone.entity.Color;
import org.fpoly.capstone.entity.Material;
import org.fpoly.capstone.entity.ProductDetail;
import org.fpoly.capstone.entity.Size;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.Voucher;
import org.fpoly.capstone.entity.VoucherDetail;
import org.fpoly.capstone.entity.enum_status.AddressStatus;
import org.fpoly.capstone.entity.enum_status.BillStatus;
import org.fpoly.capstone.entity.enum_status.BillType;
import org.fpoly.capstone.entity.enum_status.Gender;
import org.fpoly.capstone.entity.enum_status.PaymentMethod;
import org.fpoly.capstone.entity.enum_status.ProductVariantStatus;
import org.fpoly.capstone.entity.enum_status.UserRole;
import org.fpoly.capstone.entity.enum_status.UserStatus;
import org.fpoly.capstone.entity.enum_status.VoucherStatus;
import org.fpoly.capstone.repository.AddressRepository;
import org.fpoly.capstone.repository.BillDetailRepository;
import org.fpoly.capstone.repository.BillHistoryRepository;
import org.fpoly.capstone.repository.BillRepository;
import org.fpoly.capstone.repository.BrandRepository;
import org.fpoly.capstone.repository.CartRepository;
import org.fpoly.capstone.repository.CategoryRepository;
import org.fpoly.capstone.repository.ColorRepository;
import org.fpoly.capstone.repository.CustomerRepository;
import org.fpoly.capstone.repository.MaterialRepository;
import org.fpoly.capstone.repository.ProductDetailRepository;
import org.fpoly.capstone.repository.ProductRepository;
import org.fpoly.capstone.repository.SizeRepository;
import org.fpoly.capstone.repository.UserRepository;
import org.fpoly.capstone.repository.VoucherDetailRepository;
import org.fpoly.capstone.repository.VoucherRepository;
import org.fpoly.capstone.service.BillService;
import org.fpoly.capstone.service.EmailService;
import org.fpoly.capstone.service.ProductDetailService;
import org.fpoly.capstone.service.UserService;
import org.fpoly.capstone.service.VoucherService;
import org.fpoly.capstone.service.payload.bill.BuyNowBillRequest;
import org.fpoly.capstone.service.payload.bill.CreateBillRequest;
import org.fpoly.capstone.utils.general.GeneralStringCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j

@Service
@RequiredArgsConstructor


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
    private VoucherDetailRepository voucherDetailReponsitory;
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
    private final CartRepository cartRepository;
    private final UserService userService;
    private final ProductDetailService productDetailService;

    @Override
    public CreateBillRequest create(CreateBillRequest createBillDTO) {
        return new CreateBillRequest();
    }


    @Override
    public Bill createBillCode(Long idEmployee) {
        Optional<User> employee = this.userRepository.findById(idEmployee);
        if (!employee.isPresent()) {
            throw new RuntimeException("Employee not found");
        }
        Bill bill = Bill.builder()
                .code(this.generalStringCode.generateCodeAdmin())
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
        this.billRepository.save(bill);
        this.billHistoryRepository.save(BillHistory.builder()
                .status(bill.getStatus())
                .bill(bill)
                .user(bill.getUser())
                .build());
        return bill;
    }

    @Override
    public List<Bill> getBillTaoHoaDon() {
        return this.billRepository.getBillTAOHOADON();
    }

    @Transactional
    @Override
    public Bill deleteBill(Long id) {
        Optional<Bill> findIdBill = this.billRepository.findById(id);
        if (findIdBill.isPresent()) {
            Bill bill = findIdBill.get();
            this.billHistoryRepository.deleteAllByBillId(bill.getId());
            this.billRepository.delete(bill);
            return bill;
        } else {
            throw new RuntimeException("Bill not found");
        }
    }

    @Override
    public Bill save(Long id, CreateBillOfflineDTO request) {
        Optional<Bill> findIdBill = this.billRepository.findById(id);
        if (!findIdBill.isPresent()) {
            throw new RuntimeException("Bill not found");
        }
        System.out.println("Check user" + request.getIdUser());
        Optional<User> user = this.userRepository.findById(request.getIdUser());
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
        bill.setLastModifiedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        if (request.getDeliveryDate() != null) {
            bill.setShipDate(request.getDeliveryDate());
        }
        System.out.println("Check bill" + request.getType());
        if (!request.isOpenDelivery()) {
            bill.setStatus(BillStatus.THANH_CONG);
            bill.setCompletionDate(this.getCurrmentTimeStampInVN());
            this.billRepository.save(bill);
            this.billHistoryRepository.save(BillHistory.builder()
                    .status(BillStatus.THANH_CONG)
                    .bill(bill)
                    .user(bill.getEmployee())
                    .build());
            System.out.println("Check vô đây");
        } else {
            bill.setStatus(BillStatus.CHO_XAC_NHAN);
            bill.setCompletionDate(this.getCurrmentTimeStampInVN());
            this.billRepository.save(bill);
            this.billHistoryRepository.save(BillHistory.builder()
                    .status(BillStatus.CHO_XAC_NHAN)
                    .bill(bill)
                    .user(bill.getEmployee())
                    .build());
            System.out.println("Check vào day");
        }

        request.getVoucherDetails().forEach(voucher -> {
            System.out.println("Processing voucher with ID: " + voucher.getIdVoucher());
            Optional<Voucher> vouchers = this.voucherRepository.findById(voucher.getIdVoucher());
            System.out.println("Check voucher" + voucher.getIdVoucher());
            if (!vouchers.isPresent()) {
                throw new RuntimeException("Voucher not found");
            }
            if (vouchers.get().getQuantity() <= 0 && vouchers.get().getEndDate().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Voucher end date is less than current date");
            }
            vouchers.get().setQuantity(vouchers.get().getQuantity() - 1);
            this.voucherRepository.save(vouchers.get());
            VoucherDetail voucherDetail = VoucherDetail.builder()
                    .voucher(vouchers.get())
                    .bill(bill)
                    .afterPrice(new BigDecimal(voucher.getAfterVoucher()))
                    .beforePrice(new BigDecimal(voucher.getBeforVoucher()))
                    .discountPrice(new BigDecimal(voucher.getDiscountVoucher()))
                    .build();
            this.voucherDetailReponsitory.save(voucherDetail);
        });
        try {
            if (bill.getEmail() != null) {
                this.sendInVoiceEmail(bill);
            } else {
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
        Optional<Bill> findIdBill = this.billRepository.findById(id);
        if (!findIdBill.isPresent()) {
            throw new RuntimeException("Bill not found");
        }
        Bill bill = findIdBill.get();
        request.getBillDetails().forEach(detail -> {
            Optional<ProductDetail> productDetail = this.productDetailRepository.findById(Long.valueOf(detail.getIdProduct()));
            if (!productDetail.isPresent()) {
                throw new RuntimeException("Product detail not found");
            }
            if (productDetail.get().getQuantity() < detail.getQuantity()) {
                throw new RuntimeException("Product detail quantity exceeds quantity");
            }
            if (productDetail.get().getStatus() != ProductVariantStatus.DANG_SU_DUNG) {
                throw new RuntimeException("Product detail status not DANG_SU_DUNG");
            }
            Optional<BillDetail> detailBill = this.billDetailRepository.findByBillAndProductDetail(bill, productDetail.get());
            if (detailBill.isPresent()) {

                BillDetail billDetailToUpdate = detailBill.get();
                int newQuantity = billDetailToUpdate.getQuantity() + detail.getQuantity();
                billDetailToUpdate.setQuantity(newQuantity); // Cập nhật số lượng mới
                this.billDetailRepository.save(billDetailToUpdate);
                productDetail.get().setQuantity(productDetail.get().getQuantity() - detail.getQuantity());
                if (productDetail.get().getQuantity() == 0) {
                    productDetail.get().setStatus(ProductVariantStatus.HET_SAN_PHAM);
                }
                this.productDetailRepository.save(productDetail.get());
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
                this.billDetailRepository.save(billDetail);
                productDetail.get().setQuantity(productDetail.get().getQuantity() - billDetail.getQuantity());
                if (productDetail.get().getQuantity() == 0) {
                    productDetail.get().setStatus(ProductVariantStatus.HET_SAN_PHAM);
                }
                this.productDetailRepository.save(productDetail.get());
                bill.setMethod(PaymentMethod.TIEN_MAT);
                this.billRepository.save(bill);
            }
        });

        return bill;
    }

    @Override
    @Transactional
    public Bill deleteProductInBill(Long idBill, Long idProduct) {
        Optional<Bill> optional = this.billRepository.findById(idBill);
        if (!optional.isPresent()) {
            throw new RuntimeException("Bill not found");
        }
        List<BillDetail> listBillDetail = this.billDetailRepository.findByBillId(optional.get().getId());
        listBillDetail.forEach(item -> {
            Optional<ProductDetail> productDetail = this.productDetailRepository.findById(Long.valueOf(item.getProductDetail().getId()));
            if (!productDetail.isPresent()) {
                throw new RuntimeException("Product detail not found");
            }
            productDetail.get().setQuantity(productDetail.get().getQuantity() + item.getQuantity());
            if (productDetail.get().getStatus() == ProductVariantStatus.HET_SAN_PHAM) {
                productDetail.get().setStatus(ProductVariantStatus.DANG_SU_DUNG);
            }
            this.productDetailRepository.save(productDetail.get());
            this.billHistoryRepository.deleteAllByBillId(optional.get().getId());
            this.billDetailRepository.deleteByProductDetailId(idProduct);
        });
        return optional.get();
    }


    @Override
    public List<BaseAddressRequest> getAllAddressUser(Long idUser) {
        List<Object[]> listAddress = this.addressRepository.findByAddressUserIdBIll(idUser);
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
            String districtId = (String) list[5];
            String provinceId = (String) list[6];
            String wardCode = (String) list[7];
            String fullName = (String) list[8];
            String phoneNumber = (String) list[9];
            AddressStatus status = (AddressStatus) list[10];
            Long userId = (Long) list[11];
            BaseAddressRequest baseAddressRequest = new BaseAddressRequest(
                    id, line, district, province, ward, districtId, provinceId, wardCode, fullName, phoneNumber, status, userId
            );
            baseAddressRequests.add(baseAddressRequest);
        }
        return baseAddressRequests;
    }

    @Override
    public List<VoucherRequest> getVoucherMinimumbill(Integer minimumBill) {
        System.out.println("Check dữ liệu" + this.voucherRepository.getVoucherMinimumBill(minimumBill));
        List<Object[]> list = this.voucherRepository.getVoucherMinimumBill(minimumBill);
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
            VoucherRequest voucherRequest = new VoucherRequest(id, code, name, value,
                    minimumbill, quantity, startDate, endDate, voucherStatus);
            voucherRequests.add(voucherRequest);
        }
        return voucherRequests;
    }

    @Override
    public Page<ProductRequest> findAllProductDetail(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> list = this.productRepository.findAllProductDetails(pageable);
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
            String image = (String) result[12];
            ProductRequest productRequest = new ProductRequest(id, code, name, categoryName, sizeName, colorName,
                    materialName, brandName, quantity, price, gender, status, image);
            productRequests.add(productRequest);
        }
        return new PageImpl<>(productRequests, pageable, list.getTotalElements());
    }

    @Override
    public List<Voucher> getAllVoucher() {
        return this.voucherRepository.findAll();
    }

    @Override
    public CreateCustomerBill createCustomerBill(CreateCustomerBill createCustomerBill) {
        if (createCustomerBill.getCustomerName() == null || createCustomerBill.getCustomerName().isEmpty()) {
            throw new IllegalArgumentException(MessageError.NAME_NULL.getMessage());
        }
        if (createCustomerBill.getCustomerPhone() == null || createCustomerBill.getCustomerPhone().isEmpty()) {
            throw new IllegalArgumentException(MessageError.PHONE_NULL.getMessage());
        }
        if (createCustomerBill.getCustomerEmail() == null || createCustomerBill.getCustomerEmail().isEmpty()) {
            throw new IllegalArgumentException(MessageError.EMAIL_NULL.getMessage());
        }
        if (this.customerRepository.existsByPhoneNumber(createCustomerBill.getCustomerPhone())) {
            throw new IllegalArgumentException(MessageError.PHONE_ERROR.getMessage());
        }
        if (this.customerRepository.existsByEmail(createCustomerBill.getCustomerEmail())) {
            throw new IllegalArgumentException(MessageError.EMAIL_ERROR.getMessage());
        }
        User user = User.builder()
                .fullName(createCustomerBill.getCustomerName())
                .phoneNumber(createCustomerBill.getCustomerPhone())
                .email(createCustomerBill.getCustomerEmail())
                .roles(UserRole.ROLE_CUSTOMER)
                .status(UserStatus.ACTIVATED)
                .build();
        this.userRepository.save(user);
        return createCustomerBill;
    }

    @Override
    public Page<GetAllCusomter> findALlCustomerPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> results = this.customerRepository.findAllCustomerPage(pageable);
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
    public void saveToBillForOnlineUser(CreateBillRequest request) {

        User loggedUser = this.userService.getUserFromContext();
        Cart cart = this.cartRepository.findCartByUserId(loggedUser.getId());

        Bill bill = new Bill();

        User customer = cart.getUser();
        bill.setUser(customer);
        bill.setType(BillType.ONLINE);
        bill.setStatus(BillStatus.CHO_XAC_NHAN);

        double totalPrice = cart.getCartDetails().stream()
                .mapToDouble(detail -> detail.getPrice().doubleValue() * detail.getQuantity())
                .sum();
        bill.setTotalMoney(BigDecimal.valueOf(totalPrice));

        List<BillDetail> billDetailList = new ArrayList<>();
        for (CartDetail cartDetail : cart.getCartDetails()) {
            BillDetail billDetail = new BillDetail();
            billDetail.setBill(bill);
            billDetail.setProductDetail(cartDetail.getProductDetail());
            billDetail.setQuantity(cartDetail.getQuantity());
            billDetail.setPrice(cartDetail.getPrice());
            this.billDetailRepository.save(billDetail);
            billDetailList.add(billDetail);
        }

        BigDecimal moneyShip = request.getMoneyShip();
        Date recieveDate = request.getReceiveDate();
        BigDecimal grandTotal = request.getGrandTotal();
        String address = request.getAddress();
        String note = request.getNote();
        PaymentMethod paymentMethod = request.getPaymentMethod();

        bill.setTotalMoney(grandTotal);
        bill.setMoneyShip(moneyShip);
        bill.setReceiveDate(recieveDate);
        bill.setAddress(address);
        bill.setNote(note);
        bill.setMethod(paymentMethod);

        bill.setBillDetailList(billDetailList);
        this.cartRepository.deleteById(cart.getId());
        this.billRepository.save(bill);

    }

    @Override
    public Page<VoucherRequest1> findAllVoucherPage(Integer totalAmount, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> results = this.voucherRepository.findAllVoucherRequests(totalAmount, pageable);
        List<VoucherRequest1> voucherRequests = new ArrayList<>();
        for (Object[] result : results) {
            Long id = (Long) result[0];
            String code = (String) result[1];
            String name = (String) result[2];
            BigDecimal value = (BigDecimal) result[3];
            Integer minimumbill = (Integer) result[4];
            Integer quantity = (Integer) result[5];
            LocalDateTime startDate = (LocalDateTime) result[6];
            LocalDateTime endDate = (LocalDateTime) result[7];
            VoucherRequest1 voucherRequest = new VoucherRequest1(id, code, name, value, minimumbill, quantity, startDate, endDate);
            voucherRequests.add(voucherRequest);
        }
        return new PageImpl<>(voucherRequests, pageable, results.getTotalElements());
    }

    @Override
    public List<GetAllCusomter> searchCustomer(String searchQuery) {
        List<Object[]> results = this.customerRepository.searchBySearchQuery(searchQuery);
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
    public Page<ProductRequest> searchProduct(String nameProduct, Long category, Long color, Long material, Long kichCo, Long brand, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> results = this.productRepository.findAllProductDetailsBySearch(nameProduct, category, color, material, kichCo, brand, pageable);
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
            String image = (String) result[12];
            ProductRequest productRequest = new ProductRequest(id, code, name, categoryName, sizeName, colorName,
                    materialName, brandName, quantity, price, gender, status, image);
            productRequests.add(productRequest);
        }
        return new PageImpl<>(productRequests, pageable, results.getTotalElements());
    }

    @Override
    public List<Brand> findAllBrand() {
        return this.brandRepository.findAll();
    }

    @Override
    public List<Size> findAllSize() {
        return this.sizeRepository.findAll();
    }

    @Override
    public List<Category> findAllCategory() {
        return this.categoryRepository.findAll();
    }

    @Override
    public List<Material> findAllMaterial() {
        return this.materialRepository.findAll();
    }

    @Override
    public List<Color> findAllColor() {
        return this.colorRepository.findAll();
    }

    @Override
    public List<BillProductDTO> getBillDetail(Long billId) {
        List<Object[]> results = this.billDetailRepository.getProductByIDBill(billId);
        List<BillProductDTO> billDetails = new ArrayList<>();
        for (Object[] result : results) {
            Long id = (Long) result[0];
            String name = (String) result[1];
            BigDecimal price = (BigDecimal) result[2];
            Integer quantity = (Integer) result[3];
            String size = (String) result[4];
            String color = (String) result[5];
            Long idProductDetail = (Long) result[6];
            String image = (String) result[7];
            BillProductDTO billProductDTO = new BillProductDTO(id, name, price, quantity, size, color, idProductDetail, image);
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
        String htmlContent = this.emailService.generateHtmlContent(bill);
        System.out.println("Gửi email đến: " + reciprient);
        System.out.println("Chủ đề email: " + subject);
        System.out.println("Nội dung email: " + htmlContent);
        this.emailService.sendEmail(reciprient, subject, htmlContent);
    }

    @Override
    public List<Long> findAllById() {
        return this.billRepository.findByAllIds();
    }

    @Override
    public List<Bill> findByCreateDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        return this.billRepository.findByCreateDateBetween(startOfDay, endOfDay);
    }

    @Override
    public List<Bill> findAll() {
        return this.billRepository.findAll();

    }

    @Override
    public List<Bill> findByCreateDateBetween(LocalDate start, LocalDate end) {
        LocalDateTime startDate = start.atStartOfDay();
        LocalDateTime endDate = end.atTime(23, 59, 59);
        return this.billRepository.findByCreateDateBetween(startDate, endDate);
    }

    @Override
    public Bill findById(Long id) {
        Bill bill = this.billRepository.findById(id).orElseThrow();
        return bill;
    }

    @Override
    public List<Bill> findBillsByCustomerId(Long customerId) {
        return this.billRepository.getBillByCustomerId(customerId);
    }


    @Override
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

        return this.billRepository.findAll(example, pageable);
    }

    @Override
    public void buyNowForOnlineUser(BuyNowBillRequest request) {
        User loggedUser = this.userService.getUserFromContext();

        if (loggedUser == null) {
            throw new EntityNotFoundException("User not found");
        }

        //find product detail by productId and sizeId and colorId from request
        ProductDetail productDetailRequest = this.productDetailService
                .findProductDetailByIdAndSizeAndColor(request.getProductId(), request.getSizeId(), request.getColorId());

        if (productDetailRequest == null) {
            log.error("Product detail not found for Product ID: {} and Size ID: {} and ColorId: {}", request.getProductId(), request.getSizeId(), request.getColorId());
            throw new EntityNotFoundException("Product detail not found for the given product and size and color.");
        }

        //check if quantity from request is valid or not
        if (request.getQuantity() > productDetailRequest.getQuantity()) {
            log.error("Not enough product quantity: {}",
                    request.getQuantity());
            throw new RuntimeException("Not enough quantity");
        }

        Bill bill = new Bill();

        bill.setUser(loggedUser);
        bill.setType(BillType.ONLINE);
        bill.setStatus(BillStatus.CHO_XAC_NHAN);

        double totalPrice = request.getQuantity() * productDetailRequest.getPrice().doubleValue();
        bill.setTotalMoney(BigDecimal.valueOf(totalPrice));

        List<BillDetail> billDetailList = new ArrayList<>();

        BillDetail billDetail = new BillDetail();
        billDetail.setBill(bill);
        billDetail.setProductDetail(productDetailRequest);
        billDetail.setQuantity(request.getQuantity());
        billDetail.setPrice(productDetailRequest.getPrice());
        this.billDetailRepository.save(billDetail);
        billDetailList.add(billDetail);


        bill.setBillDetailList(billDetailList);
        this.billRepository.save(bill);

    }

    @Override
    public void saveToBillForBuyNow(CreateBillRequest request) {
        List<Bill> lastestBillList = this.findLastestBillByCustomerId();

        if (lastestBillList.isEmpty()) {
            throw new EntityNotFoundException("No bill found for the user.");
        }

        Bill lastestBill = lastestBillList.get(0); // Lấy hóa đơn mới nhất

        log.info("Lastest bill id: ", lastestBill.getId());

        BigDecimal moneyShip = request.getMoneyShip();
        Date receiveDate = request.getReceiveDate();
        BigDecimal grandTotal = request.getGrandTotal();
        String address = request.getAddress();
        String note = request.getNote();
        PaymentMethod paymentMethod = request.getPaymentMethod();

        // Cập nhật các giá trị của bill
        lastestBill.setTotalMoney(grandTotal);
        lastestBill.setMoneyShip(moneyShip);
        lastestBill.setReceiveDate(receiveDate);
        lastestBill.setAddress(address);
        lastestBill.setNote(note);
        lastestBill.setMethod(paymentMethod);

        // Lưu hóa đơn đã cập nhật
        this.billRepository.save(lastestBill); // Không tạo một bill mới, chỉ cập nhật hóa đơn hiện tại
    }

    @Override
    public List<Bill> findLastestBillByCustomerId() {
        User loggedUser = this.userService.getUserFromContext();

        if (loggedUser == null) {
            throw new EntityNotFoundException("User not found");
        }

        return this.billRepository.getLastestBill(loggedUser.getId(), PageRequest.ofSize(1));
    }

}
