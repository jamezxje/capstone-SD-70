package org.fpoly.capstone.repository;

import org.fpoly.capstone.dto.bill.GetAllCusomter;
import org.fpoly.capstone.entity.User;
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



}
