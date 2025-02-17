package org.fpoly.capstone.service.payload.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private Integer customerID;
    private String fullName;
    private String email;
    private String phoneNo;
    private LocalDate dateOfBirth;
    private String password;
    private String role;

}