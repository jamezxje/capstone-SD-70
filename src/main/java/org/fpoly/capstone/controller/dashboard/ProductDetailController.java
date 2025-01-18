//package org.fpoly.capstone.controller.dashboard;
//
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.fpoly.capstone.controller.payload.productdetail.ProductDetailModel;
//import org.fpoly.capstone.controller.payload.productdetail.ProductDetailViewModel;
//import org.fpoly.capstone.entity.Category;
//import org.fpoly.capstone.entity.Color;
//import org.fpoly.capstone.entity.Material;
//import org.fpoly.capstone.entity.Product;
//import org.fpoly.capstone.entity.Size;
//import org.fpoly.capstone.service.CategoryService;
//import org.fpoly.capstone.service.ColorService;
//import org.fpoly.capstone.service.MaterialService;
////import org.fpoly.capstone.service.ProductDetailService;
//import org.fpoly.capstone.service.ProductService;
//import org.fpoly.capstone.service.SizeService;
//import org.fpoly.capstone.service.payload.productdetail.ProductDetailFilterRequest;
//import org.fpoly.capstone.service.payload.productdetail.ProductDetailRequest;
////import org.fpoly.capstone.service.payload.productdetail.ProductDetailResponse;
//import org.modelmapper.ModelMapper;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.util.List;
//
//@Slf4j
//@Controller
//@RequestMapping(path = "dashboard/product-management/product-detail")
//@RequiredArgsConstructor
//public class ProductDetailController {
//
////  private final ProductDetailService productDetailService;
//  private final CategoryService categoryService;
////  private final ProductService productService;
//  private final MaterialService materialService;
//  private final ColorService colorService;
//  private final SizeService sizeService;
//  private final ModelMapper modelMapper;
//  private static final String PRODUCT_DETAIL = "productDetails";
//  private static final String PRODUCT_DETAIL_PAGE = "productDetailPage";
//  private static final String PRODUCT_DETAIL_VIEW = "/views/product-management/product-detail/product-detail-management";
//  private static final String SUCCESS_MESSAGE = "successMessage";
//  private static final String ERROR_MESSAGE = "errorMessage";
//
//  @GetMapping
//  public String onOpenProductDetailView(@RequestParam(defaultValue = "1") int page,
//                                        @RequestParam(defaultValue = "10") int size,
//                                        @RequestParam(required = false) String searchCode,
//                                        @RequestParam(required = false) String searchName,
//                                        @RequestParam(required = false) Integer searchCategoryId,
//                                        @RequestParam(required = false) Integer searchMaterialId,
//                                        @RequestParam(required = false) Integer searchColorId,
//                                        @RequestParam(required = false) Integer searchSizeId,
//                                        @RequestParam(required = false) Boolean searchStatus,
//                                        Model model) {
//
//    Pageable pageable = PageRequest.of(page - 1, size);
//
//    // Create filter request for product details
//    ProductDetailFilterRequest filterRequest = new ProductDetailFilterRequest();
//    filterRequest.setCode(searchCode);
//    filterRequest.setName(searchName);
//    filterRequest.setCategoryId(searchCategoryId);
//    filterRequest.setSizeId(searchSizeId);
//    filterRequest.setMaterialId(searchMaterialId);
//    filterRequest.setColorId(searchColorId);
//    filterRequest.setStatus(searchStatus);
//
//    //Get list attribute
//    List<Product> productList = this.productService.getAllProduct();
//    List<Category> categoryList = this.categoryService.getAllCategory();
//    List<Material> materialList = this.materialService.getAllMaterial();
//    List<Color> colorList = this.colorService.getAllColor();
//    List<Size> sizeList = this.sizeService.getAllSize();
//
//    // Get paginated product details
////    Page<ProductDetailResponse> productDetailPage = this.productDetailService
////        .searchProductDetail(filterRequest, pageable);
////
////    List<ProductDetailViewModel> viewModels = productDetailPage.getContent().stream()
////        .map(response -> {
////          ProductDetailViewModel viewModel = this.modelMapper.map(response, ProductDetailViewModel.class);
////          viewModel.setImages(response.getImages()); // Manually map images
////          return viewModel;
////        })
////        .toList();
//
////    model.addAttribute(PRODUCT_DETAIL, viewModels);
////    model.addAttribute(PRODUCT_DETAIL_PAGE, productDetailPage);
//    model.addAttribute("searchCode", searchCode);
//    model.addAttribute("searchName", searchName);
//    model.addAttribute("searchCategoryId", searchCategoryId);
//    model.addAttribute("searchMaterialId", searchMaterialId);
//    model.addAttribute("searchColorId", searchColorId);
//    model.addAttribute("searchSizeId", searchSizeId);
//    model.addAttribute("searchStatus", searchStatus);
//    model.addAttribute("productDetailModel", new ProductDetailModel());
//    model.addAttribute("editProductDetailModel", new ProductDetailModel());
//    model.addAttribute("productList", productList);
//    model.addAttribute("categoryList", categoryList);
//    model.addAttribute("materialList", materialList);
//    model.addAttribute("colorList", colorList);
//    model.addAttribute("sizeList", sizeList);
//
//    return PRODUCT_DETAIL_VIEW;
//  }
//
////  @PostMapping
////  public String createProductDetail(@Valid @ModelAttribute("productDetailModel") ProductDetailModel productDetailModel,
////                                    BindingResult result,
////                                    @RequestParam(defaultValue = "1") int page,
////                                    @RequestParam(defaultValue = "10") int size,
////                                    Model model, RedirectAttributes redirectAttributes) {
////
////    if (result.hasErrors()) {
////      Pageable pageable = PageRequest.of(page - 1, size);
////      Page<ProductDetailResponse> productDetailPage = this.productDetailService.searchProductDetail(null, pageable);
////
////      List<ProductDetailViewModel> viewModels = productDetailPage.getContent().stream()
////          .map(response -> this.modelMapper.map(response, ProductDetailViewModel.class))
////          .toList();
////
////      model.addAttribute(PRODUCT_DETAIL, viewModels);
////      model.addAttribute(PRODUCT_DETAIL_PAGE, productDetailPage);
////
////      return PRODUCT_DETAIL_VIEW;
////    }
////
////    ProductDetailRequest productDetailRequest = this.modelMapper.map(productDetailModel, ProductDetailRequest.class);
////
////    this.productDetailService.createProductDetail(productDetailRequest);
////
////    redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Product Detail created successfully!");
////
////    return "redirect:/dashboard/product-management/product-detail";
////  }
//
////  @PostMapping("/update")
////  public String updateProductDetail(@Valid @ModelAttribute("editProductDetailModel") ProductDetailModel editProductDetailModel,
////                                    BindingResult result,
////                                    @RequestParam(defaultValue = "1") int page,
////                                    @RequestParam(defaultValue = "10") int size,
////                                    Model model, RedirectAttributes redirectAttributes) {
////
////    if (result.hasErrors()) {
////      Pageable pageable = PageRequest.of(page - 1, size);
////      Page<ProductDetailResponse> productDetailPage = this.productDetailService.searchProductDetail(null, pageable);
////
////      List<ProductDetailViewModel> viewModels = productDetailPage.getContent().stream()
////          .map(response -> this.modelMapper.map(response, ProductDetailViewModel.class))
////          .toList();
////
////      model.addAttribute(PRODUCT_DETAIL, viewModels);
////      model.addAttribute(PRODUCT_DETAIL_PAGE, productDetailPage);
////
////      return PRODUCT_DETAIL_VIEW;
////    }
////
////    try {
////      ProductDetailRequest productDetailRequest = this.modelMapper.map(editProductDetailModel, ProductDetailRequest.class);
////      this.productDetailService.updateProductDetail(editProductDetailModel.getId(), productDetailRequest);
////
////      redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Product Detail updated successfully!");
////    } catch (Exception e) {
////      redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "Failed to update the product detail. Please try again.");
////    }
////
////    return "redirect:/dashboard/product-management/product-detail";
////  }
//
////  @PostMapping("/update/status/{id}")
////  public String updateProductDetailStatus(@PathVariable Integer id,
////                                          @RequestParam(defaultValue = "1") int page,
////                                          @RequestParam(defaultValue = "10") int size,
////                                          RedirectAttributes redirectAttributes) {
////
////    try {
////      this.productDetailService.changeProductDetailStatus(id);
////
////      redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Product Detail status updated successfully!");
////    } catch (Exception e) {
////      redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "Failed to update status of the product detail. Please try again.");
////    }
////
////    return "redirect:/dashboard/product-management/product-detail?page=" + page + "&size=" + size;
////  }
//
////  @PostMapping("/delete/{id}")
////  public String deleteProductDetail(@PathVariable Integer id,
////                                    @RequestParam(defaultValue = "1") int page,
////                                    @RequestParam(defaultValue = "10") int size,
////                                    RedirectAttributes redirectAttributes) {
////
////    try {
////      this.productDetailService.deleteProductDetail(id);
////
////      redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Product Detail deleted successfully!");
////    } catch (Exception e) {
////      redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "Failed to delete the product detail. Please try again.");
////    }
////
////    return "redirect:/dashboard/product-management/product-detail?page=" + page + "&size=" + size;
////  }
//}
