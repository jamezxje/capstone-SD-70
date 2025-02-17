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

@Controller
@RequestMapping(path = "dashboard/product-management/color")
@RequiredArgsConstructor
public class ColorController {

    private final ColorService colorService;
    private final ModelMapper modelMapper;
    private static final String COLORS = "colors";
    private static final String COLOR_PAGE = "colorPage";
    private static final String COLOR_VIEW = "/views/product-management/color/color-management";
    private static final String MESSAGE = "message";
    private static final String TYPE_SUCCESS = "success";
    private static final String TYPE_ERROR = "error";

    @GetMapping
    public String onOpenColorView(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(required = false) String name,
                                  @RequestParam(required = false) Boolean status,
                                  Model model) {

        Pageable pageable = PageRequest.of(page - 1, size);

        Page<ColorResponse> colorPage = this.colorService.searchColor(name, status, pageable);

        List<ColorViewModel> viewModels = colorPage.getContent().stream()
                .map(response -> this.modelMapper.map(response, ColorViewModel.class))
                .toList();

        model.addAttribute(COLORS, viewModels);
        model.addAttribute(COLOR_PAGE, colorPage);
        model.addAttribute("searchName", name);
        model.addAttribute("searchStatus", status);
        model.addAttribute("colorModel", new ColorModel());
        model.addAttribute("editColorModel", new ColorModel());

        return COLOR_VIEW;

    }

    @PostMapping
    public String createColor(@Valid @ModelAttribute("colorModel") ColorModel colorModel,
                              BindingResult result,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return this.handleColorActionErrors(page, size, model);
        }

        try {
            ColorRequest colorRequest = this.modelMapper.map(colorModel, ColorRequest.class);
            this.colorService.createColor(colorRequest);
            redirectAttributes.addFlashAttribute(MESSAGE, "Thêm màu sắc thành công");
            redirectAttributes.addFlashAttribute("type", TYPE_SUCCESS);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(MESSAGE, "Thêm màu sắc thất bại");
            redirectAttributes.addFlashAttribute("type", TYPE_ERROR);
        }

        return "redirect:/dashboard/product-management/color";

    }

    @PostMapping(path = "update")
    public String updateColor(@Valid @ModelAttribute("editColorModel") ColorModel editColorModel,
                              BindingResult result,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return this.handleColorActionErrors(page, size, model);
        }

        try {
            ColorRequest colorRequest = this.modelMapper.map(editColorModel, ColorRequest.class);

            this.colorService.updateColor(editColorModel.getId(), colorRequest);

            redirectAttributes.addFlashAttribute(MESSAGE, "Sửa màu sắc thành công");
            redirectAttributes.addFlashAttribute("type", TYPE_SUCCESS);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(MESSAGE, "Sửa màu sắc thất bại");
            redirectAttributes.addFlashAttribute("type", TYPE_ERROR);
        }

        return "redirect:/dashboard/product-management/color";

    }

    @PostMapping(path = "delete/{id}")
    public String deleteColor(@PathVariable Integer id,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size,
                              RedirectAttributes redirectAttributes) {

        try {
            this.colorService.deleteColor(id);

            redirectAttributes.addFlashAttribute(MESSAGE, "Xóa màu sắc thành công");
            redirectAttributes.addFlashAttribute("type", TYPE_SUCCESS);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(MESSAGE, "Xóa màu sắc thất bại");
            redirectAttributes.addFlashAttribute("type", TYPE_ERROR);
        }

        return "redirect:/dashboard/product-management/color?page=" + page + "&size=" + size;

    }

    private String handleColorActionErrors(int page, int size, Model model) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<ColorResponse> colorPage = this.colorService.getAllColor(pageable);

        List<ColorViewModel> viewModels = colorPage.getContent().stream()
                .map(response -> this.modelMapper.map(response, ColorViewModel.class))
                .toList();

        model.addAttribute(COLORS, viewModels);
        model.addAttribute(COLOR_PAGE, colorPage);

        return COLOR_VIEW;
    }

}
