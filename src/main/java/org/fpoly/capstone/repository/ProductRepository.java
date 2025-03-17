package org.fpoly.capstone.repository;

import org.fpoly.capstone.dto.bill.ProductRequest;
import org.fpoly.capstone.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT pd.id, p.code, p.name, c.name AS categoryName, s.name AS sizeName, " +
            "cc.name AS colorName, m.name AS materialName, b.name AS brandName, " +
            "pd.quantity, pd.price, pd.gender, pd.status " +
            "FROM Product p " +
            "JOIN p.category c " +
            "JOIN p.productDetails pd " +
            "JOIN pd.size s " +
            "JOIN pd.color cc " +
            "JOIN pd.material m " +
            "JOIN pd.brand b " +
            "ORDER BY pd.lastModifiedDate DESC")
    Page<Object[]> findAllProductDetails(Pageable pageable);

}
