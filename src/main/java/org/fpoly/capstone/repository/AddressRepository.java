package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.Address;
import org.fpoly.capstone.entity.enum_status.AddressStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("""
    SELECT ad
    FROM Address ad
    WHERE ad.user.id = :userId
    AND ad.status = :status
    """)
    Optional<Address> findDefaultAddressByUserId(@Param("userId") Long userId, @Param("status") AddressStatus status);

    List<Address> findByUserId(Long userId);
}
