package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart  c WHERE c.user.id = :userId")
    Cart findCartByUserId(@Param("userId") Long userId);

}
