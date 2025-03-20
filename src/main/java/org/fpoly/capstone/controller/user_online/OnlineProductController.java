package org.fpoly.capstone.controller.user_online;

import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.controller.payload.cart.AddProductToCartModel;
import org.fpoly.capstone.controller.payload.product_detail.ProductDetailFilterModel;
import org.fpoly.capstone.controller.payload.product_detail.ProductDetailViewModel;
import org.fpoly.capstone.entity.Color;
import org.fpoly.capstone.entity.Size;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.service.BrandService;
import org.fpoly.capstone.service.CartService;
import org.fpoly.capstone.service.CategoryService;
import org.fpoly.capstone.service.ColorService;
import org.fpoly.capstone.service.MaterialService;
import org.fpoly.capstone.service.ProductDetailService;
import org.fpoly.capstone.service.SizeService;
import org.fpoly.capstone.service.UserService;
import org.fpoly.capstone.service.payload.cart.AddProductToCartRequest;
import org.fpoly.capstone.service.payload.product_detail.ProductDetailFilterRequest;
import org.fpoly.capstone.service.payload.product_detail.ProductDetailResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/shop")
@RequiredArgsConstructor
public class OnlineProductController {

    private final ModelMapper modelMapper;
    private final ProductDetailService productDetailService;
    private final SizeService sizeService;
    private final CartService cartService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final MaterialService materialService;
    private final ColorService colorService;
    private final BrandService brandService;


    @GetMapping(path = "")
    public String onOpenProductView(ProductDetailFilterModel productDetailFilterModel, Model model) {

        ProductDetailFilterRequest request = this.modelMapper.map(productDetailFilterModel, ProductDetailFilterRequest.class);

        List<ProductDetailResponse> productDetailResponsePage = this.productDetailService.searchAvailableProductDetail(request);

        List<ProductDetailViewModel> viewModels = productDetailResponsePage.stream()
                .map(response -> this.modelMapper.map(response, ProductDetailViewModel.class))
                .toList();

        model.addAttribute("productUserResponseList", viewModels);
        model.addAttribute("categoryList", this.categoryService.getAllActiveCategory());
        model.addAttribute("materialList", this.materialService.getAllMaterial());
        model.addAttribute("colorList", this.colorService.getAllColor());
        model.addAttribute("brandList", this.brandService.getAllBrand());


        return "/views/user-online-view/products-page";
    }

    @GetMapping(path = "{productDetailId}")
    public String onOpenProductDetailView(@PathVariable(value = "productDetailId") Long productDetailId, Model model) {
        User loggedUser = this.userService.getUserFromContext();
        String loggedUserEmail = loggedUser != null ? loggedUser.getEmail() : null;  // Check if loggedUser is null

        ProductDetailResponse productDetailResponse = this.productDetailService.getProductDetailById(productDetailId);

        ProductDetailViewModel productDetailViewModel = this.modelMapper.map(productDetailResponse, ProductDetailViewModel.class);

        List<ProductDetailResponse> relatedProductDetailResponsePage = this.productDetailService.findRelatedProductDetail(productDetailId, productDetailViewModel.getBrandId());

        List<ProductDetailViewModel> viewModels = relatedProductDetailResponsePage.stream()
                .map(response -> this.modelMapper.map(response, ProductDetailViewModel.class))
                .toList();

        List<Size> sizeList = this.sizeService.getAllSize();
        List<Color> colorList = this.colorService.getColorsByProductId(productDetailViewModel.getProductId());

        model.addAttribute("product", productDetailViewModel);
        model.addAttribute("productUserResponseList", viewModels);
        model.addAttribute("relatedProductDetailList", relatedProductDetailResponsePage);
        model.addAttribute("sizeList", sizeList);
        model.addAttribute("colorList", colorList);
        model.addAttribute("productDetailId", productDetailId);
        model.addAttribute("addProductToCartModel", new AddProductToCartModel());
        model.addAttribute("loggedUserEmail", loggedUserEmail);

        return "/views/user-online-view/product-detail";
    }

    @GetMapping(path = "{productDetailId}/available-sizes")
    @ResponseBody
    public List<Long> getAvailableSizes(@PathVariable Long productDetailId, @RequestParam Long colorId) {

        ProductDetailResponse productDetailResponse = this.productDetailService.getProductDetailById(productDetailId);

        // Assuming you have a service to get sizes for the given product and color
        List<Size> availableSizes = this.sizeService.getAvailableSizesByColor(productDetailResponse.getProductId(), colorId);

        // Convert the list of available sizes to just their IDs
        return availableSizes.stream()
                .map(Size::getId)
                .collect(Collectors.toList());
    }


    @PostMapping(path = "add-to-cart")
    public String onAddingProductToCart(@ModelAttribute("addProductToCartModel") AddProductToCartModel addProductToCartModel,
                                        Model model) {

        try {
            AddProductToCartRequest addProductToCartRequest = this.modelMapper.map(addProductToCartModel, AddProductToCartRequest.class);
            this.cartService.addToCart(addProductToCartRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/shop";
    }
}
