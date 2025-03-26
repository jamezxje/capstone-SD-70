package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.BillDetail;
import org.fpoly.capstone.service.payload.bill_detail.BillDetailResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillDetailRespository extends JpaRepository<BillDetail, Long> {
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
