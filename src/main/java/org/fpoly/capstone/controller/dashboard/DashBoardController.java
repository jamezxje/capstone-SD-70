package org.fpoly.capstone.controller.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashBoardController {
  @GetMapping({"/", ""})
  public String showDashBoard() {
    return "dashboard";
  }
}
