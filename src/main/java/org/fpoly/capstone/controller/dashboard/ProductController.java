package org.fpoly.capstone.controller.dashboard;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.controller.payload.product.ProductFilterModel;
import org.fpoly.capstone.controller.payload.product.ProductModel;
import org.fpoly.capstone.controller.payload.product.ProductViewModel;
import org.fpoly.capstone.entity.Brand;
import org.fpoly.capstone.entity.Category;
import org.fpoly.capstone.entity.Color;
import org.fpoly.capstone.entity.Material;
import org.fpoly.capstone.entity.Size;
import org.fpoly.capstone.service.BrandService;
import org.fpoly.capstone.service.CategoryService;
import org.fpoly.capstone.service.ColorService;
import org.fpoly.capstone.service.MaterialService;
import org.fpoly.capstone.service.ProductDetailService;
import org.fpoly.capstone.service.ProductService;
import org.fpoly.capstone.service.SizeService;
import org.fpoly.capstone.service.payload.product.ProductFilterRequest;
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

@Controller
@RequestMapping(path = "dashboard/product-management/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final MaterialService materialService;
    private final ColorService colorService;
    private final SizeService sizeService;
    private final BrandService brandService;
    private final ProductDetailService productDetailService;
    private final ModelMapper modelMapper;
    private static final String PRODUCTS = "products";
    private static final String PRODUCT_PAGE = "productPage";
    private static final String PRODUCT_VIEW = "/views/admin-dashboard/product-management/product/product-management";
    private static final String MESSAGE = "message";
    private static final String TYPE_SUCCESS = "success";
    private static final String TYPE_ERROR = "error";

    @GetMapping
    public String onOpenProductView(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    ProductFilterModel filterModel,
                                    Model model) {

        Pageable pageable = PageRequest.of(page - 1, size);

        List<Category> categoryList = this.categoryService.getAllActiveCategory();

        ProductFilterRequest request = this.modelMapper.map(filterModel, ProductFilterRequest.class);

        Page<ProductResponse> productPage = this.productService.searchProduct(request, pageable);

        List<ProductViewModel> viewModels = productPage.getContent().stream()
                .map(response -> this.modelMapper.map(response, ProductViewModel.class))
                .toList();

        model.addAttribute("categories", categoryList);
        model.addAttribute(PRODUCTS, viewModels);
        model.addAttribute(PRODUCT_PAGE, productPage);
        model.addAttribute("request", request);
        model.addAttribute("productModel", new ProductModel());
        model.addAttribute("editProductModel", new ProductModel());

        return PRODUCT_VIEW;

    }

    @GetMapping(path = "add")
    public String onOpenAddNewProductDetailView(Model model) {
        List<Category> categoryList = this.categoryService.getAllActiveCategory();
        List<Material> materialList = this.materialService.getAllMaterial();
        List<Color> colorList = this.colorService.getAllColor();
        List<Size> sizeList = this.sizeService.getAllSize();
        List<Brand> brandList = this.brandService.getAllBrand();

        model.addAttribute("categories", categoryList);
        model.addAttribute("materials", materialList);
        model.addAttribute("colors", colorList);
        model.addAttribute("sizes", sizeList);
        model.addAttribute("brands", brandList);
        model.addAttribute("productModel", new ProductModel());

        return "/views/admin-dashboard/product-management/product/add-new-product-form";
    }

    @PostMapping
    public String createProduct(@Valid @ModelAttribute("productModel") ProductModel productModel,
                                BindingResult result,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int size,
                                Model model, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return this.handleProductActionErrors(page, size, model);
        }

        try {
            ProductRequest productRequest = this.modelMapper.map(productModel, ProductRequest.class);
            this.productService.createProduct(productRequest);
            redirectAttributes.addFlashAttribute(MESSAGE, "Thêm sản phẩm thành công");
            redirectAttributes.addFlashAttribute("type", TYPE_SUCCESS);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(MESSAGE, "Thêm sản phẩm thất bại");
            redirectAttributes.addFlashAttribute("type", TYPE_ERROR);
        }

        return "redirect:/dashboard/product-management/product";

    }

    @PostMapping(path = "update")
    public String updateProduct(@Valid @ModelAttribute("editProductModel") ProductModel editProductModel,
                                BindingResult result,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int size,
                                Model model, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return this.handleProductActionErrors(page, size, model);
        }

        try {
            ProductRequest productRequest = this.modelMapper.map(editProductModel, ProductRequest.class);

            this.productService.updateProduct(editProductModel.getId(), productRequest);

            redirectAttributes.addFlashAttribute(MESSAGE, "Sửa sản phẩm thành công");
            redirectAttributes.addFlashAttribute("type", TYPE_SUCCESS);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(MESSAGE, "Sửa sản phẩm thất bại");
            redirectAttributes.addFlashAttribute("type", TYPE_ERROR);
        }

        return "redirect:/dashboard/product-management/product";

    }

    @PostMapping(path = "delete/{id}")
    public String deleteProduct(@PathVariable Long id,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int size,
                                RedirectAttributes redirectAttributes) {

        try {
            this.productService.deleteProduct(id);

            redirectAttributes.addFlashAttribute(MESSAGE, "Xóa sản phẩm thành công");
            redirectAttributes.addFlashAttribute("type", TYPE_SUCCESS);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(MESSAGE, "Xóa sản phẩm thất bại");
            redirectAttributes.addFlashAttribute("type", TYPE_ERROR);
        }

        return "redirect:/dashboard/product-management/product?page=" + page + "&size=" + size;

    }

    private String handleProductActionErrors(int page, int size, Model model) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<ProductResponse> productPage = this.productService.getAllProduct(pageable);

        List<ProductViewModel> viewModels = productPage.getContent().stream()
                .map(response -> this.modelMapper.map(response, ProductViewModel.class))
                .toList();

        model.addAttribute(PRODUCTS, viewModels);
        model.addAttribute(PRODUCT_PAGE, productPage);

        return PRODUCT_VIEW;
    }

}
