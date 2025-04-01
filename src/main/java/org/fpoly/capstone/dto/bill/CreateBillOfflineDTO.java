package org.fpoly.capstone.dto.bill;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fpoly.capstone.dto.billDetail.CreateBillDetailRequest;
import org.fpoly.capstone.dto.voucherdetail.CreateVoucherDetailRequest;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBillOfflineDTO {
    private Long idUser;
    private String userName;
    private String phoneNumber;
    private String email;
    private String address;
    private String note;
//    @NotEmpty
    private String type;
//    @NotEmpty
    private String totalMoney;
//    @NotEmpty
    private String statusBill;
//    @NotEmpty
    private String paymentMethod;
    private String moneyShip;
    private boolean openDelivery;
    private Date lastModifiedDate;
@JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "dd/MM/yyyy")
    private Date deliveryDate;
private String itemDiscount;
//    @NotNull
    private List<CreateBillDetailRequest> billDetails;
//@NotNull
    private List<CreateVoucherDetailRequest> voucherDetails;
}

