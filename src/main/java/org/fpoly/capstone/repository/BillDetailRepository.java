package org.fpoly.capstone.repository;


import org.fpoly.capstone.entity.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillDetailRepository extends JpaRepository<BillDetail, Long> {

    List<BillDetail> findByBillId(Long id);

    @Query(value = """
                   SELECT
            bd.id AS id,
                    p.name AS name,
            bd.price AS price,
             bd.quantity AS quantity,
             s.name AS size,\s
             c.name AS color ,
                                 bd.id_product_detail AS id_product
              FROM bill_detail bd
              JOIN product_detail pd ON bd.id_product_detail = pd.id
              JOIN product p ON pd.id_product = p.id
            JOIN size s ON pd.id_size = s.id 
            JOIN color c ON pd.id_color = c.id
                WHERE bd.id_bill = :id 
            """, nativeQuery = true)
    List<Object[]> getProductByBillId(@Param("id") Long billId);

}
