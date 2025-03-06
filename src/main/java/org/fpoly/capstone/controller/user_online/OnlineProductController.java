package org.fpoly.capstone.controller.user_online;

import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.controller.payload.cart.AddProductToCartModel;
import org.fpoly.capstone.controller.payload.product_detail.ProductDetailFilterModel;
import org.fpoly.capstone.controller.payload.product_detail.ProductDetailViewModel;
import org.fpoly.capstone.entity.Size;
import org.fpoly.capstone.service.BrandService;
import org.fpoly.capstone.service.CartService;
import org.fpoly.capstone.service.CategoryService;
import org.fpoly.capstone.service.ColorService;
import org.fpoly.capstone.service.MaterialService;
import org.fpoly.capstone.service.ProductDetailService;
import org.fpoly.capstone.service.ProductService;
import org.fpoly.capstone.service.SizeService;
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

import java.util.List;

@Controller
@RequestMapping(path = "/shop")
@RequiredArgsConstructor
public class OnlineProductController {

    private final ModelMapper modelMapper;
    private final ProductDetailService productDetailService;
    private final SizeService sizeService;
    private final CartService cartService;
    private final CategoryService categoryService;
    private final ProductService productService;
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

    @GetMapping(path = "{productId}")
    public String onOpenProductDetailView(@PathVariable(value = "productId") Long productId, Model model) {

        List<Size> sizeList = this.sizeService.getAllSize();

        ProductDetailResponse productDetailResponse = this.productDetailService.getProductDetailById(productId);

        ProductDetailViewModel productDetailViewModel = this.modelMapper.map(productDetailResponse, ProductDetailViewModel.class);

        List<ProductDetailResponse> relatedProductDetailResponsePage = this.productDetailService.findRelatedProductDetail(productId, productDetailViewModel.getBrandId());

        List<ProductDetailViewModel> viewModels = relatedProductDetailResponsePage.stream()
                .map(response -> this.modelMapper.map(response, ProductDetailViewModel.class))
                .toList();

        model.addAttribute("product", productDetailViewModel);
        model.addAttribute("productUserResponseList", viewModels);
        model.addAttribute("relatedProductDetailList", relatedProductDetailResponsePage);
        model.addAttribute("sizeList", sizeList);
        model.addAttribute("addProductToCartModel", new AddProductToCartModel());

        return "/views/user-online-view/product-detail";
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
