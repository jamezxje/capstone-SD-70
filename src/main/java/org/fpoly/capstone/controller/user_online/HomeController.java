package org.fpoly.capstone.controller.user_online;

import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.controller.payload.product_detail.ProductDetailViewModel;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.service.ProductDetailService;
import org.fpoly.capstone.service.UserService;
import org.fpoly.capstone.service.payload.product_detail.ProductDetailResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductDetailService productDetailService;
    private final ModelMapper modelMapper;
    private final UserService userService;
    @GetMapping(path = "")
    public String onOpenUserHomeView(Model model) {
        User loggedUser = this.userService.getUserFromContext();
        List<ProductDetailResponse> productDetailResponsePage = this.productDetailService.getAvailableProductDetail();

        List<ProductDetailViewModel> viewModels = productDetailResponsePage.stream()
                .map(response -> this.modelMapper.map(response, ProductDetailViewModel.class))
                .toList();

        model.addAttribute("productUserResponseList", viewModels);
        model.addAttribute("loggedUser", loggedUser);
        return "/views/user-online-view/index";
    }
}
