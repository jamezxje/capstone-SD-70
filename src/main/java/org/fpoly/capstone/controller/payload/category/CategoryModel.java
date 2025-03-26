package org.fpoly.capstone.controller.payload.category;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fpoly.capstone.entity.enum_status.CategoryStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryModel {

    private Integer id;

    @NotBlank(message = "Name is required")
    private String name;

    private CategoryStatus status;

}
