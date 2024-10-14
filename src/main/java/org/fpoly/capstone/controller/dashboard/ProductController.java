package org.fpoly.capstone.controller.dashboard;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fpoly.capstone.controller.payload.product.ProductModel;
import org.fpoly.capstone.controller.payload.product.ProductViewModel;
import org.fpoly.capstone.service.ProductService;
import org.fpoly.capstone.service.payload.product.ProductRequest;
import org.fpoly.capstone.service.payload.product.ProductResponse;
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

@Slf4j
@Controller
@RequestMapping(path = "dashboard/product-management/product")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;
  private final ModelMapper modelMapper;
  private static final String PRODUCT = "products";
  private static final String PRODUCT_PAGE = "productPage";
  private static final String PRODUCT_VIEW = "/views/product-management/product/product-management";
  private static final String SUCCESS_MESSAGE = "successMessage";
  private static final String ERROR_MESSAGE = "errorMessage";

  @GetMapping
  public String onOpenProductView(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(required = false) String code,
                                  @RequestParam(required = false) String name,
                                  @RequestParam(required = false) String weight,
                                  @RequestParam(required = false) String gsmQualification,
                                  @RequestParam(required = false) Boolean status,
                                  Model model) {

    Pageable pageable = PageRequest.of(page - 1, size);

    Page<ProductResponse> productPage = this.productService
        .searchProduct(code, name, weight, gsmQualification, status, pageable);

    List<ProductViewModel> viewModels = productPage.getContent().stream()
        .map(response -> this.modelMapper.map(response, ProductViewModel.class))
        .toList();

    model.addAttribute(PRODUCT, viewModels);
    model.addAttribute(PRODUCT_PAGE, productPage);
    model.addAttribute("searchCode", code);
    model.addAttribute("searchName", name);
    model.addAttribute("searchWeight", weight);
    model.addAttribute("searchGsmQualification", gsmQualification);
    model.addAttribute("searchStatus", status);
    model.addAttribute("productModel", new ProductModel());
    model.addAttribute("editProductModel", new ProductModel());

    return PRODUCT_VIEW;
  }

  @PostMapping
  public String createProduct(@Valid @ModelAttribute("productModel") ProductModel productModel,
                              BindingResult result,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model, RedirectAttributes redirectAttributes) {

    if (result.hasErrors()) {
      Pageable pageable = PageRequest.of(page - 1, size);
      Page<ProductResponse> productPage = this.productService.getAllProduct(pageable);

      List<ProductViewModel> viewModels = productPage.getContent().stream()
          .map(response -> this.modelMapper.map(response, ProductViewModel.class))
          .toList();

      model.addAttribute(PRODUCT, viewModels);
      model.addAttribute(PRODUCT_PAGE, productPage);

      return PRODUCT_VIEW;
    }

    ProductRequest productRequest = this.modelMapper.map(productModel, ProductRequest.class);

    this.productService.createProduct(productRequest);

    redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Product created successfully!");

    return "redirect:/dashboard/product-management/product";
  }

  @PostMapping("/update")
  public String updateProduct(@Valid @ModelAttribute("editProductModel") ProductModel editProductModel,
                              BindingResult result,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model, RedirectAttributes redirectAttributes) {

    if (result.hasErrors()) {
      Pageable pageable = PageRequest.of(page - 1, size);
      Page<ProductResponse> productPage = this.productService.getAllProduct(pageable);

      List<ProductViewModel> viewModels = productPage.getContent().stream()
          .map(response -> this.modelMapper.map(response, ProductViewModel.class))
          .toList();

      model.addAttribute(PRODUCT, viewModels);
      model.addAttribute(PRODUCT_PAGE, productPage);

      return PRODUCT_VIEW;
    }

    try {
      ProductRequest productRequest = this.modelMapper.map(editProductModel, ProductRequest.class);
      this.productService.updateProduct(editProductModel.getId(), productRequest);

      redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Product updated successfully!");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "Failed to update the product. Please try again.");
    }

    return "redirect:/dashboard/product-management/product";
  }

  @PostMapping("/update/status/{id}")
  public String updateProductStatus(@PathVariable Integer id,
                                    @RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    RedirectAttributes redirectAttributes) {

    try {
      this.productService.changeProductStatus(id);

      redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Product updated status successfully!");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "Failed to update status of the product. Please try again.");
    }

    return "redirect:/dashboard/product-management/product?page=" + page + "&size=" + size;
  }

  @PostMapping("/delete/{id}")
  public String deleteProduct(@PathVariable Integer id,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size,
                              RedirectAttributes redirectAttributes) {

    try {
      this.productService.deleteProduct(id);

      redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Product deleted successfully!");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "Failed to delete the product. Please try again.");
    }

    return "redirect:/dashboard/product-management/product?page=" + page + "&size=" + size;
  }

}
