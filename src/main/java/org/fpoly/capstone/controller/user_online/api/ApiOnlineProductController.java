package org.fpoly.capstone.controller.user_online.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fpoly.capstone.service.ProductDetailService;
import org.fpoly.capstone.service.payload.product_detail.ProductDetailResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Log4j2
@RestController
@RequestMapping(path = "shop")
@RequiredArgsConstructor
public class ApiOnlineProductController {

    private final ProductDetailService productDetailService;

    @GetMapping(path = "{productDetailId}/price")
    public BigDecimal getProductDetailPrice(@PathVariable Long productDetailId, @RequestParam Long colorId, @RequestParam Long sizeId) {

        ProductDetailResponse productDetailResponse = this.productDetailService.getProductDetailById(productDetailId);

        BigDecimal productDetailPrice = this.productDetailService.findProductDetailPriceByIdAndSizeAndColor(productDetailResponse.getProductId(), colorId, sizeId);

        return productDetailPrice;
    }
}
