package org.fpoly.capstone.controller.dashboard;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.controller.payload.brand.BrandModel;
import org.fpoly.capstone.controller.payload.brand.BrandViewModel;
import org.fpoly.capstone.service.BrandService;
import org.fpoly.capstone.service.payload.brand.BrandRequest;
import org.fpoly.capstone.service.payload.brand.BrandResponse;
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
@RequestMapping(path = "dashboard/product-management/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;
    private final ModelMapper modelMapper;
    private static final String BRANDS = "brands";
    private static final String BRAND_PAGE = "brandPage";
    private static final String BRAND_VIEW = "/views/product-management/brand/brand-management";
    private static final String MESSAGE = "message";
    private static final String TYPE_SUCCESS = "success";
    private static final String TYPE_ERROR = "error";

    @GetMapping
    public String onOpenBrandView(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(required = false) String name,
                                  @RequestParam(required = false) Boolean status,
                                  Model model) {

        Pageable pageable = PageRequest.of(page - 1, size);

        Page<BrandResponse> brandPage = this.brandService.searchBrand(name, status, pageable);

        List<BrandViewModel> viewModels = brandPage.getContent().stream()
                .map(response -> this.modelMapper.map(response, BrandViewModel.class))
                .toList();

        model.addAttribute(BRANDS, viewModels);
        model.addAttribute(BRAND_PAGE, brandPage);
        model.addAttribute("searchName", name);
        model.addAttribute("searchStatus", status);
        model.addAttribute("brandModel", new BrandModel());
        model.addAttribute("editBrandModel", new BrandModel());

        return BRAND_VIEW;

    }

    @PostMapping
    public String createBrand(@Valid @ModelAttribute("brandModel") BrandModel brandModel,
                              BindingResult result,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return this.handleBrandActionErrors(page, size, model);
        }

        try {
            BrandRequest brandRequest = this.modelMapper.map(brandModel, BrandRequest.class);
            this.brandService.createBrand(brandRequest);
            redirectAttributes.addFlashAttribute(MESSAGE, "Thêm nhãn hàng thành công");
            redirectAttributes.addFlashAttribute("type", TYPE_SUCCESS);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(MESSAGE, "Thêm nhãn hàng thất bại");
            redirectAttributes.addFlashAttribute("type", TYPE_ERROR);
        }

        return "redirect:/dashboard/product-management/brand";

    }

    @PostMapping(path = "update")
    public String updateBrand(@Valid @ModelAttribute("editBrandModel") BrandModel editBrandModel,
                              BindingResult result,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return this.handleBrandActionErrors(page, size, model);
        }

        try {
            BrandRequest brandRequest = this.modelMapper.map(editBrandModel, BrandRequest.class);

            this.brandService.updateBrand(editBrandModel.getId(), brandRequest);

            redirectAttributes.addFlashAttribute(MESSAGE, "Sửa nhãn hàng thành công");
            redirectAttributes.addFlashAttribute("type", TYPE_SUCCESS);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(MESSAGE, "Sửa nhãn hàng thất bại");
            redirectAttributes.addFlashAttribute("type", TYPE_ERROR);
        }

        return "redirect:/dashboard/product-management/brand";

    }

    @PostMapping(path = "delete/{id}")
    public String deleteBrand(@PathVariable Integer id,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size,
                              RedirectAttributes redirectAttributes) {

        try {
            this.brandService.deleteBrand(id);

            redirectAttributes.addFlashAttribute(MESSAGE, "Xóa nhãn hàng thành công");
            redirectAttributes.addFlashAttribute("type", TYPE_SUCCESS);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(MESSAGE, "Xóa nhãn hàng thất bại");
            redirectAttributes.addFlashAttribute("type", TYPE_ERROR);
        }

        return "redirect:/dashboard/product-management/brand?page=" + page + "&size=" + size;

    }

    private String handleBrandActionErrors(int page, int size, Model model) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<BrandResponse> brandPage = this.brandService.getAllBrand(pageable);

        List<BrandViewModel> viewModels = brandPage.getContent().stream()
                .map(response -> this.modelMapper.map(response, BrandViewModel.class))
                .toList();

        model.addAttribute(BRANDS, viewModels);
        model.addAttribute(BRAND_PAGE, brandPage);

        return BRAND_VIEW;
    }

}
