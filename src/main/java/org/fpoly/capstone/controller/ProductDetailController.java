package org.fpoly.capstone.controller;

import org.fpoly.capstone.entity.ProductDetail;
import org.fpoly.capstone.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductDetailController {
    @Autowired
    private ProductDetailService productDetailService;
    @GetMapping("/getAllProductDetail")
    public List<ProductDetail> getAllProductDetail() {
        return productDetailService.getAllProductDetails();
    }
}
