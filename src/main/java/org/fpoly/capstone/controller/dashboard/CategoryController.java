package org.fpoly.capstone.controller.dashboard;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.controller.payload.category.CategoryModel;
import org.fpoly.capstone.controller.payload.category.CategoryViewModel;
import org.fpoly.capstone.service.CategoryService;
import org.fpoly.capstone.service.payload.category.CategoryRequest;
import org.fpoly.capstone.service.payload.category.CategoryResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(path = "dashboard/product-management/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private static final String CATEGORY = "categories";
    private static final String CATEGORY_PAGE = "categoryPage";
    private static final String CATEGORY_VIEW = "/views/product-management/category/category-management";
    private static final String SUCCESS_MESSAGE = "successMessage";

    @GetMapping
    public String onOpenCategoryView(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(required = false) String name,
                                     @RequestParam(required = false) Boolean status,
                                     Model model) {

        Pageable pageable = PageRequest.of(page - 1, size);

        Page<CategoryResponse> categoryPage = this.categoryService.searchCategory(name, status, pageable);

        List<CategoryViewModel> viewModels = categoryPage.getContent().stream()
                .map(response -> this.modelMapper.map(response, CategoryViewModel.class))
                .toList();

        model.addAttribute(CATEGORY, viewModels);
        model.addAttribute(CATEGORY_PAGE, categoryPage);
        model.addAttribute("searchName", name);
        model.addAttribute("searchStatus", status);
        model.addAttribute("categoryModel", new CategoryModel());
        model.addAttribute("editCategoryModel", new CategoryModel());

        return CATEGORY_VIEW;

    }

    @PostMapping
    public String createCategory(@Valid @ModelAttribute("categoryModel") CategoryModel categoryModel,
                                 BindingResult result,
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 Model model, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return this.handleCategoryActionErrors(page, size, model);
        }

        CategoryRequest categoryRequest = this.modelMapper.map(categoryModel, CategoryRequest.class);
        this.categoryService.createCategory(categoryRequest);

        redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Category created successfully!");

        return "redirect:/dashboard/product-management/category";

    }

    @PostMapping("/update")
    public String updateCategory(@Valid @ModelAttribute("editCategoryModel") CategoryModel editCategoryModel,
                                 BindingResult result,
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 Model model, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return this.handleCategoryActionErrors(page, size, model);
        }

        try {
            CategoryRequest categoryRequest = this.modelMapper.map(editCategoryModel, CategoryRequest.class);

            this.categoryService.updateCategory(editCategoryModel.getId(), categoryRequest);

            redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Category updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update the category. Please try again.");
        }

        return "redirect:/dashboard/product-management/category";

    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Integer id,
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 RedirectAttributes redirectAttributes) {

        try {
            this.categoryService.deleteCategory(id);

            redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Category deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete the category. Please try again.");
        }

        return "redirect:/dashboard/product-management/category?page=" + page + "&size=" + size;

    }


    private String handleCategoryActionErrors(int page, int size, Model model) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<CategoryResponse> categoryPage = this.categoryService.getAllCategory(pageable);

        List<CategoryViewModel> viewModels = categoryPage.getContent().stream()
                .map(response -> this.modelMapper.map(response, CategoryViewModel.class))
                .toList();

        model.addAttribute(CATEGORY, viewModels);
        model.addAttribute(CATEGORY_PAGE, categoryPage);

        return CATEGORY_VIEW;
    }

}
