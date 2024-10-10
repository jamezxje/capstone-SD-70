package org.fpoly.capstone.controller.dashboard;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.controller.payload.material.MaterialModel;
import org.fpoly.capstone.controller.payload.material.MaterialViewModel;
import org.fpoly.capstone.service.MaterialService;
import org.fpoly.capstone.service.payload.material.MaterialRequest;
import org.fpoly.capstone.service.payload.material.MaterialResponse;
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
@RequestMapping(path = "dashboard/product-management/material")
@RequiredArgsConstructor
public class MaterialController {

  private final MaterialService materialService;
  private final ModelMapper modelMapper;
  private static final String MATERIAL = "materials";
  private static final String MATERIAL_PAGE = "materialPage";
  private static final String MATERIAL_VIEW = "/views/product-management/material/material-management";
  private static final String SUCCESS_MESSAGE = "successMessage";

  @GetMapping
  public String onOpenMaterialView(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(required = false) String name,
                                   @RequestParam(required = false) Boolean status,
                                   Model model) {

    Pageable pageable = PageRequest.of(page - 1, size);

    Page<MaterialResponse> materialPage = this.materialService.searchMaterial(name, status, pageable);

    List<MaterialViewModel> viewModels = materialPage.getContent().stream()
        .map(response -> this.modelMapper.map(response, MaterialViewModel.class))
        .toList();

    model.addAttribute(MATERIAL, viewModels);
    model.addAttribute(MATERIAL_PAGE, materialPage);
    model.addAttribute("searchName", name);
    model.addAttribute("searchStatus", status);
    model.addAttribute("materialModel", new MaterialModel());
    model.addAttribute("editMaterialModel", new MaterialModel());

    return MATERIAL_VIEW;

  }

  @PostMapping
  public String createMaterial(@Valid @ModelAttribute("materialModel") MaterialModel materialModel,
                               BindingResult result,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int size,
                               Model model, RedirectAttributes redirectAttributes) {

    if (result.hasErrors()) {
      Pageable pageable = PageRequest.of(page - 1, size);
      Page<MaterialResponse> materialPage = this.materialService.getAllMaterial(pageable);

      List<MaterialViewModel> viewModels = materialPage.getContent().stream()
          .map(response -> this.modelMapper.map(response, MaterialViewModel.class))
          .toList();

      model.addAttribute(MATERIAL, viewModels);
      model.addAttribute(MATERIAL_PAGE, materialPage);

      return MATERIAL_VIEW;

    }

    MaterialRequest materialRequest = this.modelMapper.map(materialModel, MaterialRequest.class);
    this.materialService.createMaterial(materialRequest);

    redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Material created successfully!");

    return "redirect:/dashboard/product-management/material";

  }

  @PostMapping("/update")
  public String updateMaterial(@Valid @ModelAttribute("editMaterialModel") MaterialModel editMaterialModel,
                               BindingResult result,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int size,
                               Model model, RedirectAttributes redirectAttributes) {

    if (result.hasErrors()) {
      Pageable pageable = PageRequest.of(page - 1, size);
      Page<MaterialResponse> materialPage = this.materialService.getAllMaterial(pageable);

      List<MaterialViewModel> viewModels = materialPage.getContent().stream()
          .map(response -> this.modelMapper.map(response, MaterialViewModel.class))
          .toList();

      model.addAttribute(MATERIAL, viewModels);
      model.addAttribute(MATERIAL_PAGE, materialPage);

      return MATERIAL_VIEW;

    }

    try {
      MaterialRequest materialRequest = this.modelMapper.map(editMaterialModel, MaterialRequest.class);

      this.materialService.updateMaterial(editMaterialModel.getId(), materialRequest);

      redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Material updated successfully!");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", "Failed to update the material. Please try again.");
    }

    return "redirect:/dashboard/product-management/material";

  }

  @PostMapping("/delete/{id}")
  public String deleteMaterial(@PathVariable Integer id,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int size,
                               RedirectAttributes redirectAttributes) {

    try {
      this.materialService.deleteMaterial(id);

      redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Material deleted successfully!");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete the material. Please try again.");
    }

    return "redirect:/dashboard/product-management/material?page=" + page + "&size=" + size;

  }

}
