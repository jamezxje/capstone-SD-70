package org.fpoly.capstone.controller.dashboard;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fpoly.capstone.controller.payload.product_detail.ProductDetailFilterModel;
import org.fpoly.capstone.controller.payload.product_detail.ProductDetailModel;
import org.fpoly.capstone.controller.payload.product_detail.ProductDetailViewModel;
import org.fpoly.capstone.service.BrandService;
import org.fpoly.capstone.service.CategoryService;
import org.fpoly.capstone.service.ColorService;
import org.fpoly.capstone.service.MaterialService;
import org.fpoly.capstone.service.ProductDetailService;
import org.fpoly.capstone.service.ProductService;
import org.fpoly.capstone.service.SizeService;
import org.fpoly.capstone.service.payload.product_detail.ProductDetailFilterRequest;
import org.fpoly.capstone.service.payload.product_detail.ProductDetailRequest;
import org.fpoly.capstone.service.payload.product_detail.ProductDetailResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Log4j2
@Controller
@RequestMapping(path = "dashboard/product-management/product-detail")
@RequiredArgsConstructor
public class AdminProductDetailController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final MaterialService materialService;
    private final ColorService colorService;
    private final SizeService sizeService;
    private final BrandService brandService;
    private final ProductDetailService productDetailService;
    private final ModelMapper modelMapper;

    private static final String PRODUCT_DETAILS = "productDetails";
    private static final String PRODUCT_DETAIL_PAGE = "productDetailPage";
    private static final String PRODUCT_DETAIL_VIEW = "/views/admin-dashboard/product-management/product-detail/product-detail-management";
    private static final String MESSAGE = "message";
    private static final String TYPE_SUCCESS = "success";
    private static final String TYPE_ERROR = "error";

    private void addCommonAttributes(Model model) {
        model.addAttribute("categories", this.categoryService.getAllActiveCategory());
        model.addAttribute("materials", this.materialService.getAllMaterial());
        model.addAttribute("colors", this.colorService.getAllColor());
        model.addAttribute("sizes", this.sizeService.getAllSize());
        model.addAttribute("brands", this.brandService.getAllBrand());
        model.addAttribute("products", this.productService.getAllActiveProduct());
    }

    @GetMapping(path = "")
    public String onOpenProductDetailView(@RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          ProductDetailFilterModel productDetailFilterModel,
                                          Model model) {
        Pageable pageable = PageRequest.of(page - 1, size);

        ProductDetailFilterRequest request = this.modelMapper.map(productDetailFilterModel, ProductDetailFilterRequest.class);

        Page<ProductDetailResponse> productDetailResponsePage = this.productDetailService.searchProductDetails(request, pageable);

        List<ProductDetailViewModel> viewModels = productDetailResponsePage.getContent().stream()
                .map(response -> this.modelMapper.map(response, ProductDetailViewModel.class))
                .toList();

        this.addCommonAttributes(model);

        model.addAttribute("request", request);
        model.addAttribute(PRODUCT_DETAILS, viewModels);
        model.addAttribute(PRODUCT_DETAIL_PAGE, productDetailResponsePage);
        model.addAttribute("editProductDetailModel", new ProductDetailModel());

        return PRODUCT_DETAIL_VIEW;
    }

    @GetMapping(path = "add")
    public String onOpenAddNewProductDetailView(Model model) {
        this.addCommonAttributes(model);

        model.addAttribute("productDetailModel", new ProductDetailModel());

        return "/views/admin-dashboard/product-management/product-detail/add-new-product-detail-form";
    }

    @PostMapping(path = "add")
    public String addNewProductDetail(@Valid @ModelAttribute("productDetailModel") ProductDetailModel productDetailModel,
                                      BindingResult result,
                                      @RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      Model model, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return this.handleProductDetailActionErrors(page, size, model);
        }
        try {
            ProductDetailRequest productDetailRequest = this.modelMapper.map(productDetailModel, ProductDetailRequest.class);
            this.productDetailService.createProductDetail(productDetailRequest);
            redirectAttributes.addFlashAttribute(MESSAGE, "Thêm chi tiết sản phẩm thành công");
            redirectAttributes.addFlashAttribute("type", TYPE_SUCCESS);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(MESSAGE, "Thêm chi tiết sản phẩm thất bại");
            redirectAttributes.addFlashAttribute("type", TYPE_ERROR);
        }
        return "redirect:/dashboard/product-management/product-detail";
    }

    private String handleProductDetailActionErrors(int page, int size, Model model) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<ProductDetailResponse> productDetailPage = this.productDetailService.getAllProductDetails(pageable);

        List<ProductDetailViewModel> viewModels = productDetailPage.getContent().stream()
                .map(response -> this.modelMapper.map(response, ProductDetailViewModel.class))
                .toList();

        model.addAttribute(PRODUCT_DETAILS, viewModels);
        model.addAttribute(PRODUCT_DETAIL_PAGE, productDetailPage);

        return PRODUCT_DETAIL_VIEW;
    }

    @GetMapping(path = "update/{productDetailId}")
    public String onOpenUpdateProductDetailView(@PathVariable(value = "productDetailId") Long productDetailId,
                                                Model model) {

        ProductDetailResponse productDetailResponse = this.productDetailService.getProductDetailById(productDetailId);

        ProductDetailModel updateProductDetailModel = this.modelMapper.map(productDetailResponse, ProductDetailModel.class);

        this.addCommonAttributes(model);

        model.addAttribute("productDetailModel", updateProductDetailModel);

        return "/views/admin-dashboard/product-management/product-detail/update-product-detail-form";
    }

    @GetMapping(path = "detail/{productDetailId}")
    public String onOpenDetailProductDetailView(@PathVariable(value = "productDetailId") Long productDetailId,
                                                Model model) {

        ProductDetailResponse productDetailResponse = this.productDetailService.getProductDetailById(productDetailId);

        ProductDetailViewModel productDetailViewModel = this.modelMapper.map(productDetailResponse, ProductDetailViewModel.class);

        this.addCommonAttributes(model);

        model.addAttribute("productDetailViewModel", productDetailViewModel);

        return "/views/admin-dashboard/product-management/product-detail/view-product-detail-form";
    }


    @PostMapping(path = "update")
    public String updateProductDetail(@Valid @ModelAttribute("productDetailModel") ProductDetailModel productDetailModel,
                                      BindingResult result,
                                      @RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      Model model, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return this.handleProductDetailActionErrors(page, size, model);
        }

        try {
            ProductDetailRequest productDetailRequest = this.modelMapper.map(productDetailModel, ProductDetailRequest.class);

            this.productDetailService.updateProductDetail(productDetailModel.getId(), productDetailRequest);

            redirectAttributes.addFlashAttribute(MESSAGE, "Cập nhật chi tiết sản phẩm thành công");
            redirectAttributes.addFlashAttribute("type", TYPE_SUCCESS);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(MESSAGE, "Cập nhật chi tiết sản phẩm thất bại");
            redirectAttributes.addFlashAttribute("type", TYPE_ERROR);
        }

        return "redirect:/dashboard/product-management/product-detail";
    }

    @PostMapping(path = "delete/{productDetailId}")
    public String deleteProduct(@PathVariable Long productDetailId,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int size,
                                RedirectAttributes redirectAttributes) {

        try {
            this.productDetailService.deleteProductDetail(productDetailId);

            redirectAttributes.addFlashAttribute(MESSAGE, "Xóa chi tiết sản phẩm thành công");
            redirectAttributes.addFlashAttribute("type", TYPE_SUCCESS);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(MESSAGE, "Xóa chi tiết sản phẩm thất bại");
            redirectAttributes.addFlashAttribute("type", TYPE_ERROR);
        }

        return "redirect:/dashboard/product-management/product-detail?page=" + page + "&size=" + size;

    }

}
