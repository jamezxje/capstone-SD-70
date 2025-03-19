//package org.fpoly.capstone.service.payload.user;
//
//import jakarta.persistence.PrePersist;
//import org.fpoly.capstone.entity.base.PrimaryUser;
//
//import java.util.UUID;
//
//public class CreatePrimaryUserListener {
//
//    @PrePersist
//    private void onCreate(PrimaryUser entity) {
//        if (entity.getId() == null || entity.getId().isEmpty()) {
//            entity.setId(UUID.randomUUID().toString());
//        }
//    }
//}