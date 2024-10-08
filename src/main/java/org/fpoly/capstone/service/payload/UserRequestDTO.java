package org.fpoly.capstone.service.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {

  private Integer memberId;
  private String firstName;
  private String lastName;
  private String username;
  private String email;
  private String phoneNo;
  private LocalDate dateOfBirth;
  private String password;
  private String role;

}