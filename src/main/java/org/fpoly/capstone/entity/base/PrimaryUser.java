//package org.fpoly.capstone.entity.base;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.EntityListeners;
//import jakarta.persistence.Id;
//import jakarta.persistence.MappedSuperclass;
//import lombok.Getter;
//import lombok.Setter;
//import org.fpoly.capstone.service.payload.user.CreatePrimaryUserListener;
//
//@Getter
//@Setter
//@MappedSuperclass
//@EntityListeners(CreatePrimaryUserListener.class) // Đảm bảo Listener được kích hoạt
//public abstract class PrimaryUser{
//
//    @Id
//    @Column(length = 36, updatable = false, nullable = false)
//    private String id;
//}
