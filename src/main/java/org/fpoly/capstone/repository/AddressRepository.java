package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.Address;
import org.fpoly.capstone.entity.enum_status.AddressStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

    @Query("""
    SELECT ad
    FROM Address ad
    WHERE ad.user.id = :userId
    AND ad.addressStatus = :status
    """)
    Optional<Address> findDefaultAddressByUserId(@Param("userId") String userId, @Param("status") AddressStatus status);

}
