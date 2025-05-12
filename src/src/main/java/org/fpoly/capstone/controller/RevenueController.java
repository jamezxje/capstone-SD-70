package org.fpoly.capstone.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fpoly.capstone.entity.ProductDetail;
import org.fpoly.capstone.service.RevenueService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/dashboard/product-management/revenue")
@RequiredArgsConstructor
@Slf4j
public class RevenueController {

    private final RevenueService revenueService;

    @GetMapping("/list")
    public String getRevenuePage(Model model,@RequestParam(defaultValue = "1") Integer numPage
                                 ) {
        model.addAttribute("totalProductToday", revenueService.totalProductToday());
        model.addAttribute("totalRevenueToday", revenueService.totalRevenueToday());
        model.addAttribute("totalProductRefundToday", revenueService.totalProductRefundToday());
        model.addAttribute("totalRevenue", revenueService.totalRevenue());
        model.addAttribute("totalProduct", revenueService.totalProduct());
        model.addAttribute("totalProductRefund", revenueService.totalProductRefund());
        model.addAttribute("bestSellingProducts", revenueService.getBestSellingProduct());
        model.addAttribute("totalProductCandel", revenueService.totalProductCanel());
        model.addAttribute("totalProductCandelToday", revenueService.totalProductCanelToday());
        String timePeriod = "Ngày hôm nay";
        model.addAttribute("timePeriod", timePeriod);


        return "views/revenue/revenue";
    }

    @GetMapping("/search")
    public String searchRevenue(
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(value = "month", required = false) String monthYear,
            @RequestParam(value = "year", required = false) Integer year,
            Model model) {

        BigDecimal revenue = BigDecimal.ZERO;
        Integer totalProducts = 0;
        Integer refundProduct = 0;
        Integer totalProductCanel = 0;
        String timePeriod = "";

        log.info("(searchRevenue)"+ year);
        log.info("(searchRevenue)" + monthYear);
        if (date != null) {
            revenue = revenueService.totalRevenueDate(date);
            totalProducts = revenueService.totalProductDate(date);
            refundProduct = revenueService.totalProductRefundDate(date);
            totalProductCanel = revenueService.totalProductCanelDate(date);
            timePeriod = "Ngày " + date;


        }else if (monthYear != null && !monthYear.isEmpty() && monthYear.contains("-")){
            int yearM = Integer.parseInt(monthYear.split("-")[0]);  // Lấy năm từ "YYYY-MM"
            int month = Integer.parseInt(monthYear.split("-")[1]);
            revenue = revenueService.totalRevenueMonthAndYear(month, yearM);
            totalProducts = revenueService.totalProductMonthAndYear(month, yearM);
            refundProduct = revenueService.totalProductRefundMonthAndYear(month, yearM);
            timePeriod = "Tháng " + month +"-"+ yearM;


        } else if (year != null) {
            revenue = revenueService.totalRevenueYear(year);
            totalProducts = revenueService.totalProductYear(year);
            refundProduct = revenueService.totalProductRefundYear(year);
            totalProductCanel = revenueService.totalProductCanelYear(year);
            timePeriod = "Năm " + year;
        }

        log.info("(searchRevenue)"+ revenue);
        log.info("(searchRevenue)"+ totalProducts);
        log.info("timePeriod: " + timePeriod);  // Kiểm tra giá trị của timePeriod

        model.addAttribute("totalRevenueToday", revenue);
        model.addAttribute("totalProductToday", totalProducts);
        model.addAttribute("totalProductRefundToday", refundProduct);
        model.addAttribute("totalProductCandelToday", totalProductCanel);
        model.addAttribute("totalProductRefund", revenueService.totalProductRefund());
        model.addAttribute("totalRevenue", revenueService.totalRevenue());
        model.addAttribute("totalProduct", revenueService.totalProduct());
        model.addAttribute("bestSellingProducts", revenueService.getBestSellingProduct());
        model.addAttribute("totalProductCandel", revenueService.totalProductCanel());
        model.addAttribute("timePeriod", timePeriod);

        return "views/revenue/revenue";
    }


}
