package org.fpoly.capstone.controller.payload.color;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ColorViewModel {

  private Integer id;
  private String name;
  private Boolean status;
  private LocalDateTime createdOn;
  private LocalDateTime updatedOn;

}
