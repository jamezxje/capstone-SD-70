package org.fpoly.capstone.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.ProductDetail;
import org.fpoly.capstone.service.RevenueService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
//        List<Bill> totalsCancelledBills = List.of();


        log.info("(searchRevenue)"+ year);
        log.info("(searchRevenue)" + monthYear);
        if (date != null) {
            revenue = revenueService.totalRevenueDate(date);
            totalProducts = revenueService.totalProductDate(date);
            refundProduct = revenueService.totalProductRefundDate(date);
            totalProductCanel = revenueService.totalProductCanelDate(date);
            bestSaleProduct = revenueService.getBestSellingProductDate(date);
//            totalsCancelledBills = revenueService.totalsCancelledBillsDate(date);
            timePeriod = "Ngày " + date;


        }else if (monthYear != null && !monthYear.isEmpty() && monthYear.contains("-")){
            int yearM = Integer.parseInt(monthYear.split("-")[0]);  // Lấy năm từ "YYYY-MM"
            int month = Integer.parseInt(monthYear.split("-")[1]);
            revenue = revenueService.totalRevenueMonthAndYear(month, yearM);
            totalProducts = revenueService.totalProductMonthAndYear(month, yearM);
            refundProduct = revenueService.totalProductRefundMonthAndYear(month, yearM);
            totalProductCanel = revenueService.totalProductCanelMonthAndYear(month, yearM);
            bestSaleProduct = revenueService.getBestSellingProductMonthAndYear(month, yearM);
//            totalsCancelledBills = revenueService.totalsCancelledBillsMonth(month, yearM);
            timePeriod = "Tháng " + month +"-"+ yearM;


        } else if (year != null) {
            revenue = revenueService.totalRevenueYear(year);
            totalProducts = revenueService.totalProductYear(year);
            refundProduct = revenueService.totalProductRefundYear(year);
            totalProductCanel = revenueService.totalProductCanelYear(year);
            bestSaleProduct = revenueService.getBestSellingProductYears(year);
//            totalsCancelledBills = revenueService.totalsCancelledBillsYear(year);
            timePeriod = "Năm " + year;
        }else {
            revenue = revenueService.totalRevenueToday();
            totalProducts = revenueService.totalProductToday();
            refundProduct = revenueService.totalProductRefundToday();
            totalProductCanel = revenueService.totalProductCanelToday();
            bestSaleProduct = revenueService.getBestSellingProductToday();
//            totalsCancelledBills = revenueService.totalsCancelledBillsDate(date);

            timePeriod = "Ngày hôm nay";

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
        List<Bill> totalsCancelledBills;


        if (dateExcel != null) {
            bestSellingProductsToday = revenueService.getBestSellingProductDate(dateExcel);
            totalsCancelledBills = revenueService.totalsCancelledBillsDate(dateExcel);
        } else if (monthExcel != null && monthExcel.contains("-")) {
            int yearMonth = Integer.parseInt(monthExcel.split("-")[0]);
            int monthOnly = Integer.parseInt(monthExcel.split("-")[1]);
            bestSellingProductsToday = revenueService.getBestSellingProductMonthAndYear(monthOnly, yearMonth);
            totalsCancelledBills = revenueService.totalsCancelledBillsMonth(monthOnly, yearMonth);

        } else if (yearExcel != null) {
            bestSellingProductsToday = revenueService.getBestSellingProductYears(yearExcel);
            totalsCancelledBills = revenueService.totalsCancelledBillsYear(yearExcel);
        } else {
            bestSellingProductsToday =revenueService.getBestSellingProductToday();
            totalsCancelledBills = revenueService.totalsCancelledBillsToday();
        }

        List<String> billInfos = totalsCancelledBills.stream()
                .map(bill -> "Bill{id=" + bill.getId() + ", code=" + bill.getCode() + "}")
                .collect(Collectors.toList());

        log.info("(Hoa don): {}", billInfos);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Doanh thu");

        // ======= 1. Style =======
        Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 20);
        titleFont.setBold(true);
        titleFont.setColor(IndexedColors.DARK_BLUE.getIndex());

        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);

        Font bodyFont = workbook.createFont();
        bodyFont.setFontHeightInPoints((short) 13);

        IndexedColors[] bgColors = {
                IndexedColors.LIGHT_YELLOW,      // Tổng doanh thu
                IndexedColors.LIGHT_GREEN,       // Tổng số lượng
                IndexedColors.LIGHT_ORANGE,      // Trả hàng
                IndexedColors.LIGHT_TURQUOISE,   // Hủy
                IndexedColors.LIGHT_CORNFLOWER_BLUE // Thời gian
        };

        CellStyle[] cellStyles = new CellStyle[5];
        for (int i = 0; i < 5; i++) {
            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(bgColors[i].getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setFont(headerFont);
            cellStyles[i] = style;
        }

        // ======= 2. Tiêu đề chính =======
        int columnOffset = 2;
        Row titleRow = sheet.createRow(0);
        titleRow.setHeightInPoints(100); // tăng chiều cao hàng
        Cell titleCell = titleRow.createCell(columnOffset);
        titleCell.setCellValue("THỐNG KÊ CỬA HÀNG BÁN QUẦN ÁO THỂ THAO POLY SPORT");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, columnOffset, columnOffset + 4));

        // ======= 3. Header =======
        Row headerRow = sheet.createRow(1);
        headerRow.setHeightInPoints(25);
        String[] headers = {
                "Tổng doanh thu", "Tổng số lượng sản phẩm", "Hủy đơn hàng", "Thời gian"
        };


        // Tạo formatter cho tiền tệ VN
        NumberFormat vndFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

// Format tiền tệ
        String formattedRevenue = vndFormat.format(totalRevenueToday).replace("₫", "VNĐ");

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(columnOffset + i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(cellStyles[i]);
            sheet.setColumnWidth(columnOffset + i, 35 * 256); // Tăng độ rộng cột
        }

        // ======= 4. Dòng dữ liệu =======
        Row dataRow = sheet.createRow(2);
        dataRow.setHeightInPoints(42);

        Object[] values = {
                formattedRevenue,
                totalProductToday,
                totalProductCandelToday,
                timePeriod
        };

        for (int i = 0; i < values.length; i++) {
            Cell cell = dataRow.createCell(columnOffset + i);
            if (values[i] instanceof String) {
                cell.setCellValue((String) values[i]);
            } else if (values[i] instanceof Number) {
                cell.setCellValue(((Number) values[i]).doubleValue());
            }
            // Clone style từng cột và đổi font thành bodyFont
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.cloneStyleFrom(cellStyles[i]);
            dataStyle.setFont(bodyFont);
            cell.setCellStyle(dataStyle);
        }



        // ======= 5. Tạo 1 hàng trống cách biệt =======
        // Bắt đầu tạo bảng sản phẩm bán chạy, cách 1 hàng trống sau phần doanh thu
        int rowIndex = 4; // giả sử phần doanh thu kết thúc ở dòng 2, dòng 3 để trống

// Thêm tiêu đề TOP SẢN PHẨM BÁN CHẠY
        Row bestSellingTitleRow = sheet.createRow(rowIndex++);
        bestSellingTitleRow.setHeightInPoints(60);
        Cell bestSellingTitleCell = bestSellingTitleRow.createCell(columnOffset);
        bestSellingTitleCell.setCellValue("TOP SẢN PHẨM BÁN CHẠY");

// Tạo style cho tiêu đề (giống style tiêu đề chính)
        CellStyle bestSellingTitleStyle = workbook.createCellStyle();
        Font bestSellingTitleFont = workbook.createFont();
        bestSellingTitleFont.setFontHeightInPoints((short) 16);
        bestSellingTitleFont.setBold(true);
        bestSellingTitleStyle.setFont(bestSellingTitleFont);
        bestSellingTitleStyle.setAlignment(HorizontalAlignment.CENTER);
        bestSellingTitleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        bestSellingTitleCell.setCellStyle(bestSellingTitleStyle);

// Merge ô cho tiêu đề từ cột columnOffset đến columnOffset + 2 (vì bảng có 3 cột)
        sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, columnOffset, columnOffset + 2));

// Style căn giữa cho dữ liệu SPBC
        CellStyle baseCenterStyle = workbook.createCellStyle();
        baseCenterStyle.setAlignment(HorizontalAlignment.CENTER);
        baseCenterStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        baseCenterStyle.setFont(bodyFont);
        baseCenterStyle.setBorderTop(BorderStyle.THIN);
        baseCenterStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        baseCenterStyle.setBorderBottom(BorderStyle.THIN);
        baseCenterStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        baseCenterStyle.setBorderLeft(BorderStyle.THIN);
        baseCenterStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        baseCenterStyle.setBorderRight(BorderStyle.THIN);
        baseCenterStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());

// Style header SPBC (màu duy nhất, căn giữa)
        CellStyle productHeaderStyle = workbook.createCellStyle();
        productHeaderStyle.cloneStyleFrom(baseCenterStyle);
        productHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        productHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        productHeaderStyle.setBorderTop(BorderStyle.THIN);
        productHeaderStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        productHeaderStyle.setBorderBottom(BorderStyle.THIN);
        productHeaderStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        productHeaderStyle.setBorderLeft(BorderStyle.THIN);
        productHeaderStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        productHeaderStyle.setBorderRight(BorderStyle.THIN);
        productHeaderStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        Font productHeaderFont = workbook.createFont();
        productHeaderFont.setBold(true);
        productHeaderFont.setFontHeightInPoints((short) 14);
        productHeaderStyle.setFont(productHeaderFont);

// Header SPBC
        Row productHeader = sheet.createRow(rowIndex++);
        productHeader.setHeightInPoints(25);
        String[] productHeaders = {"STT", "Tên sản phẩm", "Số lượng đã bán"};
        for (int i = 0; i < productHeaders.length; i++) {
            Cell cell = productHeader.createCell(columnOffset + i);
            cell.setCellValue(productHeaders[i]);
            cell.setCellStyle(productHeaderStyle);
            sheet.setColumnWidth(columnOffset + i, 35 * 256);
        }

// Dữ liệu SPBC
        int stt = 1;
        for (Map.Entry<ProductDetail, Integer> entry : bestSellingProductsToday) {
            Row row = sheet.createRow(rowIndex++);
            row.setHeightInPoints(40);

            for (int i = 0; i < 3; i++) {
                Cell cell = row.createCell(columnOffset + i);
                cell.setCellStyle(baseCenterStyle);
                if (i == 0) {
                    cell.setCellValue(stt++);
                } else if (i == 1) {
                    cell.setCellValue(entry.getKey().getProduct().getName());
                } else if (i == 2) {
                    cell.setCellValue(entry.getValue());
                }
            }
        }




        // Tạo 1 hàng trống cách biệt trước bảng HÓA ĐƠN ĐÃ HỦY
        rowIndex++;

// Thêm tiêu đề bảng HÓA ĐƠN ĐÃ HỦY
        Row cancelledBillsTitleRow = sheet.createRow(rowIndex++);
        cancelledBillsTitleRow.setHeightInPoints(40);
        Cell cancelledBillsTitleCell = cancelledBillsTitleRow.createCell(columnOffset);
        cancelledBillsTitleCell.setCellValue("THỐNG KÊ CÁC HOÁ ĐƠN ĐÃ HUỶ");

// Style tiêu đề giống bảng SPBC
        CellStyle cancelledBillsTitleStyle = workbook.createCellStyle();
        Font cancelledBillsTitleFont = workbook.createFont();
        cancelledBillsTitleFont.setFontHeightInPoints((short) 16);
        cancelledBillsTitleFont.setBold(true);
        cancelledBillsTitleStyle.setFont(cancelledBillsTitleFont);
        cancelledBillsTitleStyle.setAlignment(HorizontalAlignment.CENTER);
        cancelledBillsTitleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cancelledBillsTitleCell.setCellStyle(cancelledBillsTitleStyle);

// Merge tiêu đề trên 6 cột (bạn có 6 cột cho bảng hủy đơn)
        sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, columnOffset, columnOffset + 5));

// Header bảng HÓA ĐƠN ĐÃ HỦY
        Row cancelledHeaderRow = sheet.createRow(rowIndex++);
        cancelledHeaderRow.setHeightInPoints(25);
        String[] cancelledHeaders = {"Thời gian hủy", "Mã hóa đơn", "Loại Hóa Đơn", "Tên Khách Hàng", "Tên Nhân Viên", "Số Tiền Hủy", "Lý do hủy"};

        CellStyle cancelledHeaderStyle = workbook.createCellStyle();
        cancelledHeaderStyle.cloneStyleFrom(baseCenterStyle);
        cancelledHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        cancelledHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font cancelledHeaderFont = workbook.createFont();
        cancelledHeaderFont.setBold(true);
        cancelledHeaderFont.setFontHeightInPoints((short) 14);
        cancelledHeaderStyle.setFont(cancelledHeaderFont);

        for (int i = 0; i < cancelledHeaders.length; i++) {
            Cell cell = cancelledHeaderRow.createCell(columnOffset + i);
            cell.setCellValue(cancelledHeaders[i]);
            cell.setCellStyle(cancelledHeaderStyle);
            sheet.setColumnWidth(columnOffset + i, 30 * 256); // Độ rộng cột vừa phải
        }

        BigDecimal totalCancelledAmount = totalsCancelledBills.stream()
                .map(Bill::getTotalMoney)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        currencyFormat.setMaximumFractionDigits(0);

// Dữ liệu các hóa đơn đã hủy
        for (Bill bill : totalsCancelledBills) {
            Row row = sheet.createRow(rowIndex++);
            row.setHeightInPoints(35);

            // Thời gian hủy (giả sử có getter getCancelledTime() kiểu Date/LocalDateTime)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            Cell cell0 = row.createCell(columnOffset);
            cell0.setCellValue(bill.getLastModifiedDate() != null ? bill.getLastModifiedDate().format(formatter) : "");
            cell0.setCellStyle(baseCenterStyle);

            // Mã hóa đơn (giả sử getCode())
            Cell cell1 = row.createCell(columnOffset + 1);
            cell1.setCellValue(bill.getCode() != null ? bill.getCode() : "");
            cell1.setCellStyle(baseCenterStyle);

            // Loại Hóa Đơn (giả sử getBillType())
            Cell cell2 = row.createCell(columnOffset + 2);
            cell2.setCellValue(bill.getType() != null ? bill.getType().name() : "");
            cell2.setCellStyle(baseCenterStyle);

            // Tên Khách Hàng (giả sử getCustomer().getName())
            Cell cell3 = row.createCell(columnOffset + 3);
            cell3.setCellValue(bill.getUser() != null ? bill.getUser().getFullName() : "");
            cell3.setCellStyle(baseCenterStyle);

            // Tên Nhân Viên (giả sử getEmployee().getName())
            Cell cell4 = row.createCell(columnOffset + 4);
            cell4.setCellValue(bill.getEmployee() != null ? bill.getEmployee().getFullName() : "");
            cell4.setCellStyle(baseCenterStyle);


            Cell cell5 = row.createCell(columnOffset + 5);
            if (bill.getTotalMoney() != null) {
                String formattedMoney = currencyFormat.format(bill.getTotalMoney()).replace("₫", " VNĐ");
                cell5.setCellValue(formattedMoney);
            } else {
                cell5.setCellValue("");
            }
            cell5.setCellStyle(baseCenterStyle);

            // Lý do hủy (giả sử getCancelReason())
            Cell cell6 = row.createCell(columnOffset + 6);
            cell6.setCellValue(bill.getNote() != null ? bill.getNote() : "");
            cell6.setCellStyle(baseCenterStyle);
        }
        Row totalRow = sheet.createRow(rowIndex++);
        totalRow.setHeightInPoints(30);

// Style cho ô label "Tổng tiền đã huỷ"
        CellStyle totalLabelStyle = workbook.createCellStyle();
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        totalLabelStyle.setFont(boldFont);
        totalLabelStyle.setAlignment(HorizontalAlignment.CENTER);
        totalLabelStyle.setVerticalAlignment(VerticalAlignment.CENTER);

// Style cho ô giá trị tổng tiền huỷ - màu đỏ
        CellStyle redMoneyStyle = workbook.createCellStyle();
        Font redFont = workbook.createFont();
        redFont.setColor(IndexedColors.RED.getIndex());
        redFont.setBold(true);
        redFont.setFontHeightInPoints((short) 14); // 👈 Font to lên
        redMoneyStyle.setFont(redFont);
        redMoneyStyle.setFont(redFont);
        redMoneyStyle.setAlignment(HorizontalAlignment.RIGHT);
        redMoneyStyle.setVerticalAlignment(VerticalAlignment.CENTER);

// Ô hiển thị "TỔNG TIỀN ĐÃ HUỶ:" (cột "Số Tiền Hủy" = offset + 5)
        Cell totalLabelCell = totalRow.createCell(columnOffset + 4);
        totalLabelCell.setCellValue("TỔNG TIỀN ĐÃ HUỶ:");
        totalLabelCell.setCellStyle(totalLabelStyle);

// Ô hiển thị tổng số tiền đã huỷ (ở cột kế bên = offset + 6)
        Cell totalValueCell = totalRow.createCell(columnOffset + 5);
        String formattedTotal = currencyFormat.format(totalCancelledAmount).replace("₫", " VNĐ");
        totalValueCell.setCellValue(formattedTotal);
        totalValueCell.setCellStyle(redMoneyStyle);


        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=doanhthu.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }




}
