package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OnlineAddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT a FROM Address a WHERE a.user.id = :userId ORDER BY a.createDate DESC")
    List<Address> findAddressByUserId(@Param("userId") Long userId);


    @Query("SELECT a FROM Address a WHERE a.user.id = :userId AND a.status = org.fpoly.capstone.entity.enum_status.AddressStatus.DANG_SU_DUNG")
    Address findDefaultAddressByUserId(@Param("userId") Long userId);

}
