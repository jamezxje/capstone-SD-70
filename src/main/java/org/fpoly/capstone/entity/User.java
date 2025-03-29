package org.fpoly.capstone.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fpoly.capstone.entity.enum_status.UserRole;
import org.fpoly.capstone.entity.enum_status.UserStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Vui lòng nhập họ và tên")
    @Column(name = "full_name", length = 50)
    private String fullName;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd") // Định dạng theo input type="date"
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @NotEmpty(message = "Vui lòng nhập số điện thoại")
    @Pattern(regexp = "^(0\\d{9})$", message = "Số điện thoại không hợp lệ")
    @Column(name = "phone_number", length = 10)
    private String phoneNumber;

    @NotEmpty(message = "Vui lòng nhập email")
    @Email(message = "Email không hợp lệ")
    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "gender")
    private Boolean gender;

    @Column(name = "avatar", length = 255)
    private String avatar;

    @Column(name = "citizen_identity", length = 200)
    private String citizenIdentity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;

    @NotEmpty(message = "Vui lòng nhập mật khẩu")
    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "roles")
    private UserRole roles;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "last_modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Column(name = "created_by", length = 255)
    private String createdBy;

    @Column(name = "updated_by", length = 255)
    private String updatedBy;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    @JsonManagedReference
    private List<Address> addresses;

}
