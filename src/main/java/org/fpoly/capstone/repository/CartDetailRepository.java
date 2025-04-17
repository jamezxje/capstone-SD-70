package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.CartDetail;
import org.fpoly.capstone.service.payload.cart_detail.CartDetailResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {

    @Query("SELECT NEW org.fpoly.capstone.service.payload.cart_detail.CartDetailResponse(" +
            "cd.id, " +
            "cd.productDetail.id, " +
            "cd.productDetail.product.name, " +
            "cd.productDetail.featureImage, " +
            "cd.productDetail.size.name, " +
            "cd.productDetail.color.name, " +
            "cd.productDetail.price, " +
            "cd.quantity) " +
            "FROM CartDetail cd " +
            "WHERE cd.cart.user.id = :userId ORDER BY cd.createDate DESC")
    List<CartDetailResponse> findCartDetailByUserId(@Param("userId") Long userId);
}
