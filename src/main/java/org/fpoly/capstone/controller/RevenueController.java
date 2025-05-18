package org.fpoly.capstone.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.fpoly.capstone.entity.ProductDetail;
import org.fpoly.capstone.service.RevenueService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        model.addAttribute("bestSellingProductsToday", revenueService.getBestSellingProductToday());
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
        List<Map.Entry<ProductDetail, Integer>> bestSaleProduct = null;
        String timePeriod = "";

        log.info("(searchRevenue)"+ year);
        log.info("(searchRevenue)" + monthYear);
        if (date != null) {
            revenue = revenueService.totalRevenueDate(date);
            totalProducts = revenueService.totalProductDate(date);
            refundProduct = revenueService.totalProductRefundDate(date);
            totalProductCanel = revenueService.totalProductCanelDate(date);
            bestSaleProduct = revenueService.getBestSellingProductDate(date);
            timePeriod = "Ngày " + date;


        }else if (monthYear != null && !monthYear.isEmpty() && monthYear.contains("-")){
            int yearM = Integer.parseInt(monthYear.split("-")[0]);  // Lấy năm từ "YYYY-MM"
            int month = Integer.parseInt(monthYear.split("-")[1]);
            revenue = revenueService.totalRevenueMonthAndYear(month, yearM);
            totalProducts = revenueService.totalProductMonthAndYear(month, yearM);
            refundProduct = revenueService.totalProductRefundMonthAndYear(month, yearM);
            totalProductCanel = revenueService.totalProductCanelMonthAndYear(month, yearM);
            bestSaleProduct = revenueService.getBestSellingProductMonthAndYear(month, yearM);
            timePeriod = "Tháng " + month +"-"+ yearM;


        } else if (year != null) {
            revenue = revenueService.totalRevenueYear(year);
            totalProducts = revenueService.totalProductYear(year);
            refundProduct = revenueService.totalProductRefundYear(year);
            totalProductCanel = revenueService.totalProductCanelYear(year);
            bestSaleProduct = revenueService.getBestSellingProductYears(year);
            timePeriod = "Năm " + year;
        }

        log.info("(searchRevenue)"+ revenue);
        log.info("(searchRevenue)"+ totalProducts);
        log.info("timePeriod: " + timePeriod);  // Kiểm tra giá trị của timePeriod

        model.addAttribute("dateExcel", date != null ? date.toString() : "");
        model.addAttribute("monthExcel", monthYear != null ? monthYear : "");
        model.addAttribute("yearExcel", year != null ? year.toString() : "");
        model.addAttribute("totalRevenueToday", revenue);
        model.addAttribute("totalProductToday", totalProducts);
        model.addAttribute("totalProductRefundToday", refundProduct);
        model.addAttribute("totalProductCandelToday", totalProductCanel);
        model.addAttribute("bestSellingProductsToday", bestSaleProduct);
        model.addAttribute("totalProductRefund", revenueService.totalProductRefund());
        model.addAttribute("totalRevenue", revenueService.totalRevenue());
        model.addAttribute("totalProduct", revenueService.totalProduct());
        model.addAttribute("bestSellingProducts", revenueService.getBestSellingProduct());
        model.addAttribute("totalProductCandel", revenueService.totalProductCanel());
        model.addAttribute("timePeriod", timePeriod);

        return "views/revenue/revenue";
    }


    @GetMapping("/export")
    public void exportExcelToday(HttpServletResponse response,
                                 @RequestParam String timePeriod,
                                 @RequestParam double totalRevenueToday,
                                 @RequestParam int totalProductToday,
                                 @RequestParam int totalProductRefundToday,
                                 @RequestParam int totalProductCandelToday,
                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateExcel,
                                 @RequestParam(required = false) String monthExcel,
                                 @RequestParam(required = false) Integer yearExcel) throws IOException {


        List<Map.Entry<ProductDetail, Integer>> bestSellingProductsToday;

        if (dateExcel != null) {
            bestSellingProductsToday = revenueService.getBestSellingProductDate(dateExcel);
        } else if (monthExcel != null && monthExcel.contains("-")) {
            int yearMonth = Integer.parseInt(monthExcel.split("-")[0]);
            int monthOnly = Integer.parseInt(monthExcel.split("-")[1]);
            bestSellingProductsToday = revenueService.getBestSellingProductMonthAndYear(monthOnly, yearMonth);
        } else if (yearExcel != null) {
            bestSellingProductsToday = revenueService.getBestSellingProductYears(yearExcel);
        } else {
            bestSellingProductsToday =revenueService.getBestSellingProductToday();
        }
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Doanh thu");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Tổng doanh thu");
        header.createCell(1).setCellValue("Tổng số lượng sản phẩm");
        header.createCell(2).setCellValue("Trả hàng");
        header.createCell(3).setCellValue("Hủy đơn hàng");
        header.createCell(4).setCellValue("Thời gian");

        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(totalRevenueToday);
        dataRow.createCell(1).setCellValue(totalProductToday);
        dataRow.createCell(2).setCellValue(totalProductRefundToday);
        dataRow.createCell(3).setCellValue(totalProductCandelToday);
        dataRow.createCell(4).setCellValue(timePeriod);

        int rowIndex = 3;
        Row productHeader = sheet.createRow(rowIndex++);
        productHeader.createCell(0).setCellValue("STT");
        productHeader.createCell(1).setCellValue("Tên sản phẩm");
        productHeader.createCell(2).setCellValue("Số lượng đã bán");

        // 5. Dữ liệu sản phẩm bán chạy
        int stt = 1;
        for (Map.Entry<ProductDetail, Integer> entry : bestSellingProductsToday) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(stt++);
            row.createCell(1).setCellValue(entry.getKey().getProduct().getName()); // Tên sản phẩm
            row.createCell(2).setCellValue(entry.getValue()); // Số lượng đã bán
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=doanhthu.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }



}
