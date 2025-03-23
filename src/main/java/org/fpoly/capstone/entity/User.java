package org.fpoly.capstone.entity;

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
    @GeneratedValue(strategy = GenerationType.UUID) // ✅ Sử dụng UUID
    @Column(length = 36, updatable = false, nullable = false)
    private String id;

    @NotEmpty(message = "Vui lòng không để trống họ và tên")
    @Pattern(regexp = "^[\\p{L} ]{1,50}$", message = "Họ và tên phải là chữ và tối đa 50 ký tự")
    @Column(name = "full_name", length = 50)
    private String fullName;

//    @NotNull(message = "Vui lòng không để trống ngày sinh")
//    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd") // Định dạng theo input type="date"
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @NotEmpty(message = "Vui lòng không để trống số điện thoại")
    @Pattern(regexp = "^(0\\d{9})$", message = "Số điện thoại phải bắt đầu từ 0 (10 số)")
    @Column(name = "phone_number", length = 10)
    private String phoneNumber;

    @NotEmpty(message = "Vui lòng không để trống email")
    @Pattern(regexp = "^[a-zA-Z0-9][a-zA-Z0-9._%+-]*@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",message = "Email không đúng định dạng")
    @Column(name = "email", length = 255)
    private String email;

//    @NotNull(message = "Vui lòng không để trống giới tính")
    @Column(name = "gender")
    private Boolean gender;

    @Column(name = "avatar", length = 255)
    private String avatar;

//    @NotEmpty(message = "Vui lòng không để trống CCCD")
//    @Pattern(regexp = "^[0-9]{12}$", message = "Căn cước công dân phải gồm 12 chữ số")
    @Column(name = "citizen_identity", length = 200)
    private String citizenIdentity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;

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
    private List<Address> addresses;

}
