package org.fpoly.capstone.controller.dashboard;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.controller.payload.size.SizeModel;
import org.fpoly.capstone.controller.payload.size.SizeViewModel;
import org.fpoly.capstone.service.SizeService;
import org.fpoly.capstone.service.payload.size.SizeRequest;
import org.fpoly.capstone.service.payload.size.SizeResponse;
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
@RequestMapping(path = "dashboard/product-management/size")
@RequiredArgsConstructor
public class SizeController {

  private final SizeService sizeService;
  private final ModelMapper modelMapper;
  private static final String SIZE = "sizes";
  private static final String SIZE_PAGE = "sizePage";
  private static final String SIZE_VIEW = "/views/product-management/size/size-management";
  private static final String SUCCESS_MESSAGE = "successMessage";

  @GetMapping
  public String onOpenSizeView(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(required = false) String name,
                               @RequestParam(required = false) Boolean status,
                               Model model) {

    Pageable pageable = PageRequest.of(page - 1, size);

    Page<SizeResponse> sizePage = this.sizeService.searchSize(name, status, pageable);

    List<SizeViewModel> viewModels = sizePage.getContent().stream()
        .map(response -> this.modelMapper.map(response, SizeViewModel.class))
        .toList();

    model.addAttribute(SIZE, viewModels);
    model.addAttribute(SIZE_PAGE, sizePage);
    model.addAttribute("searchName", name);
    model.addAttribute("searchStatus", status);
    model.addAttribute("sizeModel", new SizeModel());
    model.addAttribute("editSizeModel", new SizeModel());

    return SIZE_VIEW;

  }

  @PostMapping
  public String createSize(@Valid @ModelAttribute("sizeModel") SizeModel sizeModel,
                           BindingResult result,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "10") int size,
                           Model model, RedirectAttributes redirectAttributes) {

    if (result.hasErrors()) {
      Pageable pageable = PageRequest.of(page - 1, size);
      Page<SizeResponse> sizePage = this.sizeService.getAllSize(pageable);

      List<SizeViewModel> viewModels = sizePage.getContent().stream()
          .map(response -> this.modelMapper.map(response, SizeViewModel.class))
          .toList();

      model.addAttribute(SIZE, viewModels);
      model.addAttribute(SIZE_PAGE, sizePage);

      return SIZE_VIEW;

    }

    SizeRequest sizeRequest = this.modelMapper.map(sizeModel, SizeRequest.class);
    this.sizeService.createSize(sizeRequest);

    redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Size created successfully!");

    return "redirect:/dashboard/product-management/size";

  }

  @PostMapping("/update")
  public String updateSize(@Valid @ModelAttribute("editSizeModel") SizeModel editSizeModel,
                           BindingResult result,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "10") int size,
                           Model model, RedirectAttributes redirectAttributes) {

    if (result.hasErrors()) {
      Pageable pageable = PageRequest.of(page - 1, size);
      Page<SizeResponse> sizePage = this.sizeService.getAllSize(pageable);

      List<SizeViewModel> viewModels = sizePage.getContent().stream()
          .map(response -> this.modelMapper.map(response, SizeViewModel.class))
          .toList();

      model.addAttribute(SIZE, viewModels);
      model.addAttribute(SIZE_PAGE, sizePage);

      return SIZE_VIEW;

    }

    try {
      SizeRequest sizeRequest = this.modelMapper.map(editSizeModel, SizeRequest.class);

      this.sizeService.updateSize(editSizeModel.getId(), sizeRequest);

      redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Size updated successfully!");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", "Failed to update the size. Please try again.");
    }

    return "redirect:/dashboard/product-management/size";

  }

  @PostMapping("/delete/{id}")
  public String deleteSize(@PathVariable Integer id,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "10") int size,
                           RedirectAttributes redirectAttributes) {

    try {
      this.sizeService.deleteSize(id);

      redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Size deleted successfully!");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete the size. Please try again.");
    }

    return "redirect:/dashboard/product-management/size?page=" + page + "&size=" + size;

  }

}
