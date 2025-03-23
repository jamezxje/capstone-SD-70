package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.enum_status.UserRole;
import org.fpoly.capstone.entity.enum_status.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<User, String> {
    //List<User> findByRoles(UserRole role);
//    Page<User> findByRolesAndStatus(UserRole role, UserStatus status, Pageable pageable);
    User findByEmail(String email);

    Page<User> findByRolesAndStatus(UserRole role, UserStatus status, Pageable pageable);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.addresses WHERE u.id = :userId")
    Optional<User> findCustomerAddresses(@Param("userId") String userId);

    @Query("SELECT u FROM  User u WHERE u.phoneNumber =:phoneNumber")
    User getCustomerBySDT(@Param("phoneNumber") String phoneNumber);

    @Query("SELECT u FROM  User u WHERE u.email =:email")
    User getCustomerByEmail(@Param("email") String email);

    @Query("SELECT u FROM  User u WHERE u.citizenIdentity =:citizenIdentity")
    User getCustomerByCCCD(@Param("citizenIdentity") String citizenIdentity);

    User getById(String id);
}
