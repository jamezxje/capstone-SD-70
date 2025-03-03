package org.fpoly.capstone.controller.user_online;

import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.controller.payload.product_detail.ProductDetailViewModel;
import org.fpoly.capstone.service.ProductDetailService;
import org.fpoly.capstone.service.ProductService;
import org.fpoly.capstone.service.payload.product_detail.ProductDetailResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(path = "shop")
@RequiredArgsConstructor
public class OnlineProductController {

    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final ProductDetailService productDetailService;

    @GetMapping(path = "")
    public String onOpenProductView(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    Model model) {

        List<ProductDetailResponse> productDetailResponsePage = this.productDetailService.getAvailableProductDetail();

        List<ProductDetailViewModel> viewModels = productDetailResponsePage.stream()
                .map(response -> this.modelMapper.map(response, ProductDetailViewModel.class))
                .toList();

        model.addAttribute("productUserResponseList", viewModels);

        return "/views/user-online-view/products-page";
    }
}
