package org.fpoly.capstone.controller.dashboard;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fpoly.capstone.controller.payload.product.ProductModel;
import org.fpoly.capstone.controller.payload.productdetail.ProductDetailViewModel;
import org.fpoly.capstone.entity.Category;
import org.fpoly.capstone.entity.Color;
import org.fpoly.capstone.entity.Material;
import org.fpoly.capstone.entity.Size;
import org.fpoly.capstone.service.CategoryService;
import org.fpoly.capstone.service.ColorService;
import org.fpoly.capstone.service.MaterialService;
import org.fpoly.capstone.service.ProductDetailService;
import org.fpoly.capstone.service.ProductService;
import org.fpoly.capstone.service.SizeService;
import org.fpoly.capstone.service.payload.product.ProductRequest;
import org.fpoly.capstone.service.payload.productdetail.ProductDetailFilterRequest;
import org.fpoly.capstone.service.payload.productdetail.ProductDetailResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(path = "dashboard/product-management/product")
@RequiredArgsConstructor
public class ProductController {
    private final CategoryService categoryService;
    private final MaterialService materialService;
    private final ColorService colorService;
    private final SizeService sizeService;
    private final ProductService productService;
    private final ProductDetailService productDetailService;
    private final ModelMapper modelMapper;

    @GetMapping("/add")
    public String onOpenAddNewProductView(Model model) {
        List<Category> categoryList = this.categoryService.getAllCategory();
        List<Material> materialList = this.materialService.getAllMaterial();
        List<Color> colorList = this.colorService.getAllColor();
        List<Size> sizeList = this.sizeService.getAllSize();

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("materialList", materialList);
        model.addAttribute("colorList", colorList);
        model.addAttribute("sizeList", sizeList);
        model.addAttribute("productModel", new ProductModel());

        return "/views/product-management/product/add-new-product-form";
    }

//  @GetMapping
//  public String onOpenProductView(@RequestParam(defaultValue = "1") int page,
//                                  @RequestParam(defaultValue = "10") int size,
//                                  @RequestParam(required = false) String code,
//                                  @RequestParam(required = false) String name,
//                                  @RequestParam(required = false) String weight,
//                                  @RequestParam(required = false) String gsmQualification,
//                                  @RequestParam(required = false) Boolean status,
//                                  Model model) {
//
//    Pageable pageable = PageRequest.of(page - 1, size);
//
//    Page<ProductResponse> productPage = this.productService
//        .searchProduct(code, name, weight, gsmQualification, status, pageable);
//
//    List<ProductViewModel> viewModels = productPage.getContent().stream()
//        .map(response -> this.modelMapper.map(response, ProductViewModel.class))
//        .toList();
//
//    model.addAttribute(PRODUCT, viewModels);
//    model.addAttribute(PRODUCT_PAGE, productPage);
//    model.addAttribute("searchCode", code);
//    model.addAttribute("searchName", name);
//    model.addAttribute("searchWeight", weight);
//    model.addAttribute("searchGsmQualification", gsmQualification);
//    model.addAttribute("searchStatus", status);
//    model.addAttribute("productModel", new ProductModel());
//    model.addAttribute("editProductModel", new ProductModel());
//
//    return PRODUCT_VIEW;
//  }

    @GetMapping
    public String onOpenProductDetailView(@RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(required = false) String searchCode,
                                          @RequestParam(required = false) String searchName,
                                          @RequestParam(required = false) Integer searchCategoryId,
                                          @RequestParam(required = false) Integer searchMaterialId,
                                          @RequestParam(required = false) Integer searchColorId,
                                          @RequestParam(required = false) Integer searchSizeId,
                                          @RequestParam(required = false) Boolean searchStatus,
                                          Model model) {

        Pageable pageable = PageRequest.of(page - 1, size);

        // Create filter request for product details
        ProductDetailFilterRequest filterRequest = new ProductDetailFilterRequest();
        filterRequest.setCode(searchCode);
        filterRequest.setName(searchName);
        filterRequest.setCategoryId(searchCategoryId);
        filterRequest.setSizeId(searchSizeId);
        filterRequest.setMaterialId(searchMaterialId);
        filterRequest.setColorId(searchColorId);
        filterRequest.setStatus(searchStatus);

        //Get list attribute
//    List<Product> productList = this.productService.getAllProduct();
        List<Category> categoryList = this.categoryService.getAllCategory();
        List<Material> materialList = this.materialService.getAllMaterial();
        List<Color> colorList = this.colorService.getAllColor();
        List<Size> sizeList = this.sizeService.getAllSize();

        // Get paginated product details
        Page<ProductDetailResponse> productDetailPage = this.productDetailService
                .searchProductDetail(filterRequest, pageable);

        List<ProductDetailViewModel> viewModels = productDetailPage.getContent().stream()
                .map(response -> {
                    ProductDetailViewModel viewModel = this.modelMapper.map(response, ProductDetailViewModel.class);
                    viewModel.setImages(response.getImages()); // Manually map images
                    return viewModel;
                })
                .toList();

        model.addAttribute("productDetails", viewModels);
        model.addAttribute("productDetailPage", productDetailPage);
        model.addAttribute("searchCode", searchCode);
        model.addAttribute("searchName", searchName);
        model.addAttribute("searchCategoryId", searchCategoryId);
        model.addAttribute("searchMaterialId", searchMaterialId);
        model.addAttribute("searchColorId", searchColorId);
        model.addAttribute("searchSizeId", searchSizeId);
        model.addAttribute("searchStatus", searchStatus);
//    model.addAttribute("productDetailModel", new ProductDetailModel());
//    model.addAttribute("editProductDetailModel", new ProductDetailModel());
//    model.addAttribute("productList", productList);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("materialList", materialList);
        model.addAttribute("colorList", colorList);
        model.addAttribute("sizeList", sizeList);

        return "/views/product-management/product/product-management";
    }

    @PostMapping
    public String createProduct(@Valid @ModelAttribute("productModel") ProductModel productModel,
                                BindingResult result,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int size,
                                Model model, RedirectAttributes redirectAttributes) {


        ProductRequest productRequest = this.modelMapper.map(productModel, ProductRequest.class);

        this.productService.createProduct(productRequest);

        return "redirect:/dashboard/product-management/product";
    }
//
//  @PostMapping("/update")
//  public String updateProduct(@Valid @ModelAttribute("editProductModel") ProductModel editProductModel,
//                              BindingResult result,
//                              @RequestParam(defaultValue = "1") int page,
//                              @RequestParam(defaultValue = "10") int size,
//                              Model model, RedirectAttributes redirectAttributes) {
//
//    if (result.hasErrors()) {
//      Pageable pageable = PageRequest.of(page - 1, size);
//      Page<ProductResponse> productPage = this.productService.getAllProduct(pageable);
//
//      List<ProductViewModel> viewModels = productPage.getContent().stream()
//          .map(response -> this.modelMapper.map(response, ProductViewModel.class))
//          .toList();
//
//      model.addAttribute(PRODUCT, viewModels);
//      model.addAttribute(PRODUCT_PAGE, productPage);
//
//      return PRODUCT_VIEW;
//    }
//
//    try {
//      ProductRequest productRequest = this.modelMapper.map(editProductModel, ProductRequest.class);
//      this.productService.updateProduct(editProductModel.getId(), productRequest);
//
//      redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Product updated successfully!");
//    } catch (Exception e) {
//      redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "Failed to update the product. Please try again.");
//    }
//
//    return "redirect:/dashboard/product-management/product";
//  }
//
//  @PostMapping("/update/status/{id}")
//  public String updateProductStatus(@PathVariable Integer id,
//                                    @RequestParam(defaultValue = "1") int page,
//                                    @RequestParam(defaultValue = "10") int size,
//                                    RedirectAttributes redirectAttributes) {
//
//    try {
//      this.productService.changeProductStatus(id);
//
//      redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Product updated status successfully!");
//    } catch (Exception e) {
//      redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "Failed to update status of the product. Please try again.");
//    }
//
//    return "redirect:/dashboard/product-management/product?page=" + page + "&size=" + size;
//  }
//
//  @PostMapping("/delete/{id}")
//  public String deleteProduct(@PathVariable Integer id,
//                              @RequestParam(defaultValue = "1") int page,
//                              @RequestParam(defaultValue = "10") int size,
//                              RedirectAttributes redirectAttributes) {
//
//    try {
//      this.productService.deleteProduct(id);
//
//      redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Product deleted successfully!");
//    } catch (Exception e) {
//      redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "Failed to delete the product. Please try again.");
//    }
//
//    return "redirect:/dashboard/product-management/product?page=" + page + "&size=" + size;
//  }

}
