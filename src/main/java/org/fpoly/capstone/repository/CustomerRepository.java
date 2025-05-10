package org.fpoly.capstone.repository;

import org.fpoly.capstone.dto.bill.GetAllCusomter;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.enum_status.UserRole;
import org.fpoly.capstone.entity.enum_status.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<User, Long> {
    List<User> findAll();

    Optional<User> findById(Long id);

    Boolean existsByPhoneNumber(String phoneNumber);

    Boolean existsByEmail(String email);

    @Query("""
            select u.id as id ,
            u.fullName as fullName ,
            u.phoneNumber as phoneNumber , 
             u.email as email , 
              u.lastModifiedDate as lastModifiedDate 
              FROM User u where u.roles = 'ROLE_CUSTOMER'
              order by u.lastModifiedDate desc
            """)
    Page<Object[]> findAllCustomerPage(Pageable pageable);

    @Query("SELECT u.id as id, " +
            "u.fullName as fullName, " +
            "u.phoneNumber as phoneNumber, " +
            "u.email as email, " +
            "u.lastModifiedDate as lastModifiedDate " +
            "FROM User u WHERE " +
            "(:searchQuery IS NULL OR u.fullName LIKE %:searchQuery% OR u.phoneNumber LIKE %:searchQuery% OR u.email LIKE %:searchQuery%) " +
            "AND u.roles = 'ROLE_CUSTOMER'")
    List<Object[]> searchBySearchQuery(
            @Param("searchQuery") String searchQuery
    );



//    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.roles = :role ORDER BY u.lastModifiedDate DESC")
    Page<User> findCustomersSortedByLastModifiedDate(@Param("role") UserRole role, Pageable pageable);

//    Page<User> findByRolesAndStatus(UserRole role, UserStatus status, Pageable pageable);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.addresses WHERE u.id = :userId")
    Optional<User> findCustomerAddresses(@Param("userId") Long userId);

    @Query("SELECT u FROM User u WHERE u.phoneNumber = :phoneNumber")
    Optional<User> getEmployBySDT(@Param("phoneNumber") String phoneNumber);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> getEmployByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.citizenIdentity = :citizenIdentity")
    Optional<User> getEmployByCCCD(@Param("citizenIdentity") String citizenIdentity);

    @Query("SELECT u FROM User u WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:status IS NULL OR u.status = :status) " +
            "AND u.roles = :role")
    Page<User> searchAndFilterCustomer(@Param("keyword") String keyword,
                                        @Param("status") UserStatus status,
                                        @Param("role") UserRole role,
                                        Pageable pageable);

    User getById(Long id);

}
