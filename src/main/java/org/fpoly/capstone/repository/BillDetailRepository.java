package org.fpoly.capstone.repository;

import org.fpoly.capstone.dto.billDetail.BillDetailRequest;
import org.fpoly.capstone.dto.billDetail.BillProductDTO;
import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.BillDetail;
import org.fpoly.capstone.entity.ProductDetail;

import org.fpoly.capstone.entity.BillDetail;
import org.fpoly.capstone.service.payload.bill_detail.BillDetailResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillDetailRepository extends JpaRepository<BillDetail, Long> {
    Optional<BillDetail> findByBillAndProductDetail(Bill bill, ProductDetail productDetail);

    @Query(value = """
                   SELECT
            pd.id AS id,
                    p.name AS name,
            bd.price AS price,
             bd.quantity AS quantity,
             s.name AS size,\s
             c.name AS color ,
                                 bd.id_product_detail AS id_product ,
                               pd.feature_image AS image
              FROM bill_detail bd
              JOIN product_detail pd ON bd.id_product_detail = pd.id
              JOIN product p ON pd.id_product = p.id
            JOIN size s ON pd.id_size = s.id 
            JOIN color c ON pd.id_color = c.id
                WHERE bd.id_bill = :billId 
            """, nativeQuery = true)
    List<Object[]> getProductByIDBill(@Param("billId") Long billId);

    @Query("SELECT bd FROM BillDetail bd WHERE bd.bill.id = :billId")
    List<BillDetail> findByBillId(Long billId);

    void deleteByProductDetailId(Long idProduct);
    List<BillDetailRequest> findBillDetailByBill(Bill bill);

    Long id(Long id);
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

    @Query("SELECT bd FROM BillDetail bd WHERE bd.createDate BETWEEN :startOfDay AND :endOfDay")
    List<BillDetail> findByCreateDateBetween(@Param("startOfDay") Date startOfDay, @Param("endOfDay") Date endOfDay);
    @Query("SELECT NEW org.fpoly.capstone.service.payload.bill_detail.BillDetailResponse(" +
            "bd.id, " +
            "bd.productDetail.id, " +
            "bd.productDetail.product.name, " +
            "bd.productDetail.featureImage, " +
            "bd.productDetail.size.name, " +
            "bd.productDetail.color.name, " +
            "bd.productDetail.price, " +
            "bd.quantity) " +
            "FROM BillDetail bd " +
            "WHERE bd.bill.id = :billId ORDER BY bd.createDate DESC ")
    List<BillDetailResponse> findBillDetailByBillId(@Param("billId") Long billId);
}

