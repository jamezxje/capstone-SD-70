package org.fpoly.capstone.repository;

import org.fpoly.capstone.dto.address.BaseAddressRequest;
import org.fpoly.capstone.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Objects;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
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
    List<Object[]> findByAddressUserIdBIll(@Param("userId") Long userId);

}
