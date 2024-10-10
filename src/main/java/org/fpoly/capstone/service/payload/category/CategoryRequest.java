package org.fpoly.capstone.service.payload.category;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {

  private Integer id;

  @NotBlank(message = "Name is required")
  private String name;

  private Boolean status;

}
