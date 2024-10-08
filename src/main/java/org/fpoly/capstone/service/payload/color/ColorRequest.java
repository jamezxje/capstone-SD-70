package org.fpoly.capstone.service.payload.color;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ColorRequest {

  private Integer id;

  @NotBlank(message = "Name is required")
  private String name;

  private Boolean status;

}
