package org.fpoly.capstone.controller.user_online;

import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.controller.payload.cart.AddProductToCartModel;
import org.fpoly.capstone.controller.payload.cart_detail.CartDetailViewModel;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.repository.CartRepository;
import org.fpoly.capstone.service.CartDetailService;
import org.fpoly.capstone.service.CartService;
import org.fpoly.capstone.service.UserService;
import org.fpoly.capstone.service.payload.cart.AddProductToCartRequest;
import org.fpoly.capstone.service.payload.cart_detail.CartDetailResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(path = "cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartRepository cartRepository;
    private final CartDetailService cartDetailService;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @GetMapping(path = "")
    public String onOpenCartView(Model model) {

        User loggedUser = this.userService.getUserFromContext();

        if (loggedUser == null) {
            return "views/auth/login";
        }

        List<CartDetailResponse> cartDetailResponseList = this.cartDetailService.findCartDetailByUserId();

        List<CartDetailViewModel> viewModels = cartDetailResponseList.stream()
                .map(response -> this.modelMapper.map(response, CartDetailViewModel.class))
                .toList();

        model.addAttribute("cartDetailList", viewModels);
        model.addAttribute("shoppingCart", this.cartRepository.findCartByUserId(loggedUser.getId()));

        return "/views/user-online-view/cart-management";
    }

    @PostMapping(path = "")
    public String onAddingProductToCart(@ModelAttribute("addProductToCartModel") AddProductToCartModel addProductToCartModel,
                                        Model model) {
        User loggedUser = this.userService.getUserFromContext();

        if (loggedUser == null) {
            return "views/auth/login";
        }

        try {
            AddProductToCartRequest addProductToCartRequest = this.modelMapper.map(addProductToCartModel, AddProductToCartRequest.class);
            this.cartService.addToCart(addProductToCartRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/cart";
    }

    @GetMapping(path = "delete/{id}")
    public String deleteCartDetail(@PathVariable Long id,
                                   RedirectAttributes redirectAttributes) {

        try {
            this.cartDetailService.deleteCartDetail(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/cart";

    }
}
