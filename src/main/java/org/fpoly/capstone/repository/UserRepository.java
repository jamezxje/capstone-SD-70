package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    Optional<User> findById(Long id);

//    Optional<User> findByEmailPassword(String email);

    @Query("SELECT u FROM User u WHERE u.phoneNumber = :phoneNumber")
    Optional<User> getUserBySDT(@Param("phoneNumber") String phoneNumber);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> getUserByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.citizenIdentity = :citizenIdentity")
    Optional<User> getUserByCCCD(@Param("citizenIdentity") String citizenIdentity);

    Optional<User> findByFullName(String fullName);
}
