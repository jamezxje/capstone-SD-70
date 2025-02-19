package org.fpoly.capstone.service.payload.brand;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fpoly.capstone.entity.enum_status.BrandStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrandRequest {

    private Integer id;

    @NotBlank(message = "Name is required")
    private String name;

    private BrandStatus status;

}
