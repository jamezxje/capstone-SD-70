package org.fpoly.capstone.controller.user_online;

import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.controller.payload.product_detail.ProductDetailViewModel;
import org.fpoly.capstone.service.ProductDetailService;
import org.fpoly.capstone.service.payload.product_detail.ProductDetailResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = "/shop")
@RequiredArgsConstructor
public class OnlineProductController {

    private final ModelMapper modelMapper;
    private final ProductDetailService productDetailService;

    @GetMapping(path = "")
    public String onOpenProductView(Model model) {

        List<ProductDetailResponse> productDetailResponsePage = this.productDetailService.getAvailableProductDetail();

        List<ProductDetailViewModel> viewModels = productDetailResponsePage.stream()
                .map(response -> this.modelMapper.map(response, ProductDetailViewModel.class))
                .toList();

        model.addAttribute("productUserResponseList", viewModels);

        return "/views/user-online-view/products-page";
    }

    @GetMapping(path = "{productId}")
    public String onOpenProductDetailView(@PathVariable(value = "productId") Long productId, Model model) {

        ProductDetailResponse productDetailResponse = this.productDetailService.getProductDetailById(productId);

        ProductDetailViewModel productDetailViewModel = this.modelMapper.map(productDetailResponse, ProductDetailViewModel.class);

        List<ProductDetailResponse> relatedProductDetailResponsePage = this.productDetailService.findRelatedProductDetail(productId, productDetailViewModel.getBrandId());

        List<ProductDetailViewModel> viewModels = relatedProductDetailResponsePage.stream()
                .map(response -> this.modelMapper.map(response, ProductDetailViewModel.class))
                .toList();

        model.addAttribute("product", productDetailViewModel);
        model.addAttribute("productUserResponseList", viewModels);
        model.addAttribute("relatedProductDetailList", relatedProductDetailResponsePage);

        return "/views/user-online-view/product-detail";
    }
}
