package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.enum_status.UserRole;
import org.fpoly.capstone.entity.enum_status.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<User, Long> {

    List<User> findByRolesAndStatus(UserRole role, UserStatus status);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.addresses WHERE u.id = :userId")
    Optional<User> findUserAddresses(@Param("userId") Long userId);

    @Query("SELECT u FROM  User u WHERE u.phoneNumber =:phoneNumber")
    User getEmployBySDT(@Param("phoneNumber") String phoneNumber);

    @Query("SELECT u FROM  User u WHERE u.email =:email")
    User getEmployByEmail(@Param("email") String email);

    @Query("SELECT u FROM  User u WHERE u.citizenIdentity =:citizenIdentity")
    User getEmployByCCCD(@Param("citizenIdentity") String citizenIdentity);
}
