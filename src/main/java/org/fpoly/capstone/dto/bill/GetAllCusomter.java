package org.fpoly.capstone.dto.bill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class GetAllCusomter {
    private Long id;
    private String fullName;
    private String numberPhone;
    private String email;
    private Date lastModifiedDate;
}
