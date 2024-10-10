package org.fpoly.capstone.controller.payload.material;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaterialViewModel {

  private Integer id;
  private String name;
  private Boolean status;
  private LocalDateTime createdOn;
  private LocalDateTime updatedOn;

}
