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

    Address getAddressByUserIdAndStatus(Long id, AddressStatus status);

    @Query("""
        SELECT ad 
        FROM Address ad 
        WHERE ad.user.id = :userId 
        AND ad.status = 'DANG_SU_DUNG'
        """)
    Optional<Address> findDefaultAddressByUserId(@Param("userId") Long userId);

    @Query("""
        SELECT
         ad.id as id ,
         ad.line as line ,
         ad.district as district ,
         ad.province as province,
         ad.ward as ward,
         ad.toDistrictId as districtId,
         ad.provinceId as provinceId,
         ad.wardCode as wardCode ,
         ad.fullName as fullName ,
         ad.phoneNumber as phoneNumber ,
         ad.status as status ,
          ad.user.id as userId
         from Address ad where ad.user.id = :userId
        """)
    List<Object[]> findByUserId(@Param("userId") Long userId);

}
