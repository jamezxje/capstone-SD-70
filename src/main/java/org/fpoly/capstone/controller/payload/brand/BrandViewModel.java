package org.fpoly.capstone.controller.payload.brand;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fpoly.capstone.entity.enum_status.BrandStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrandViewModel {

    private Integer id;
    private String name;
    private BrandStatus status;

}
