package org.fpoly.capstone.dto.billDetail;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

@Getter
@Service
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInForCustomer {
    private String customerName;
    private String numberPhone;
    private String customerAddress;
    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "dd/MM/yyyy")
    private Date shipDate;
    private String moneyShip;
}
