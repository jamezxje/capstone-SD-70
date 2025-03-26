package org.fpoly.capstone.controller.payload.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fpoly.capstone.entity.enum_status.CategoryStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryViewModel {

    private Integer id;
    private String name;
    private CategoryStatus status;

}
