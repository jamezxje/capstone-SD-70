package org.fpoly.capstone.controller.dashboard;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.controller.payload.color.ColorModel;
import org.fpoly.capstone.controller.payload.color.ColorViewModel;
import org.fpoly.capstone.service.ColorService;
import org.fpoly.capstone.service.payload.color.ColorRequest;
import org.fpoly.capstone.service.payload.color.ColorResponse;
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
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "dashboard/product-management/color")
@RequiredArgsConstructor
public class ColorController {

  private final ColorService colorService;
  private final ModelMapper modelMapper;

  @GetMapping
  public String onOpenColorView(@RequestParam(defaultValue = "1") int page,  // Start page index from 1
                                @RequestParam(defaultValue = "1") int size,
                                Model model) {
    Pageable pageable = PageRequest.of(page - 1, size);  // Subtract 1 to adjust for 0-based index
    Page<ColorResponse> colorPage = this.colorService.getAllColor(pageable);

    List<ColorViewModel> viewModels = colorPage.getContent().stream()
        .map(response -> this.modelMapper.map(response, ColorViewModel.class))
        .collect(Collectors.toList());

    model.addAttribute("colors", viewModels);
    model.addAttribute("colorPage", colorPage);
    model.addAttribute("colorModel", new ColorModel());
    model.addAttribute("editColorModel", new ColorModel());

    return "/views/product-management/color/color-management";
  }


  @PostMapping
  public String createColor(@Valid @ModelAttribute("colorModel") ColorModel colorModel,
                            BindingResult result,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "1") int size,
                            Model model, RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) {
      Pageable pageable = PageRequest.of(page - 1, size);
      Page<ColorResponse> colorPage = this.colorService.getAllColor(pageable);

      List<ColorViewModel> viewModels = colorPage.getContent().stream()
          .map(response -> this.modelMapper.map(response, ColorViewModel.class))
          .collect(Collectors.toList());

      model.addAttribute("colors", viewModels);
      model.addAttribute("colorPage", colorPage);

      return "/views/product-management/color/color-management";
    }

    ColorRequest colorRequest = this.modelMapper.map(colorModel, ColorRequest.class);
    this.colorService.createColor(colorRequest);

    redirectAttributes.addFlashAttribute("successMessage", "Color created successfully!");

    return "redirect:/dashboard/product-management/color";
  }


  @PostMapping("/update")
  public String updateColor(@Valid @ModelAttribute("editColorModel") ColorModel editColorModel,
                            BindingResult result,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "1") int size,
                            Model model, RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) {
      Pageable pageable = PageRequest.of(page - 1, size);
      Page<ColorResponse> colorPage = this.colorService.getAllColor(pageable);

      List<ColorViewModel> viewModels = colorPage.getContent().stream()
          .map(response -> this.modelMapper.map(response, ColorViewModel.class))
          .collect(Collectors.toList());

      model.addAttribute("colors", viewModels);
      model.addAttribute("colorPage", colorPage);

      return "/views/product-management/color/color-management";
    }

    try {
      ColorRequest colorRequest = this.modelMapper.map(editColorModel, ColorRequest.class);
      this.colorService.updateColor(editColorModel.getId(), colorRequest);

      redirectAttributes.addFlashAttribute("successMessage", "Color updated successfully!");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", "Failed to update the color. Please try again.");
    }

    return "redirect:/dashboard/product-management/color";
  }

  @PostMapping("/delete/{id}")
  public String deleteColor(@PathVariable Integer id,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "1") int size,
                            RedirectAttributes redirectAttributes) {
    try {
      this.colorService.deleteColor(id);
      redirectAttributes.addFlashAttribute("successMessage", "Color deleted successfully!");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete the color. Please try again.");
    }

    // After deletion, redirect to the paginated list
    return "redirect:/dashboard/product-management/color?page=" + page + "&size=" + size;
  }

}
