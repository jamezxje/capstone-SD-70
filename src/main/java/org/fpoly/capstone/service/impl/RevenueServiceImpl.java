package org.fpoly.capstone.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.dto.DailyStatisticsDTO;
import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.BillDetail;
import org.fpoly.capstone.entity.ProductDetail;
import org.fpoly.capstone.entity.enum_status.BillStatus;
import org.fpoly.capstone.service.BillDetailService;
import org.fpoly.capstone.service.BillService;
import org.fpoly.capstone.service.RevenueService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RevenueServiceImpl implements RevenueService {

    private final BillService billService;
    private final BillDetailService billDetailService;

    @Override
    public BigDecimal totalRevenue() {
        List<Bill> totalBill = billService.findAll();
        return totalBill.stream()
                .filter(b -> b.getStatus() == BillStatus.THANH_CONG)
                .map(b -> {
                    BigDecimal shipCost = b.getMoneyShip() != null ? b.getMoneyShip() : BigDecimal.ZERO;
                    BigDecimal discount = b.getItemDiscount() != null ? b.getItemDiscount() : BigDecimal.ZERO;
                    return b.getTotalMoney().subtract(shipCost).subtract(discount);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Integer totalProduct(){
        List<BillDetail> bills = billDetailService.findAll();
        return bills.stream()
                        .filter(bd -> bd.getStatusBill() == BillStatus.THANH_CONG)
                        .mapToInt(BillDetail::getQuantity)
                        .sum();
    }

    @Override
    public Integer totalProductRefund() {
        List<BillDetail> bills = billDetailService.findAll();
        return bills.stream()
                .filter(bd -> bd.getStatusBill() == BillStatus.TRA_HANG)
                .mapToInt(BillDetail::getQuantity)
                .sum();
    }

    @Override
    public Integer totalProductCanel() {
        List<BillDetail> bills = billDetailService.findAll();
        return bills.stream()
                .filter(bd -> bd.getStatusBill() == BillStatus.DA_HUY)
                .mapToInt(BillDetail::getQuantity)
                .sum();
    }

    @Override
    public Integer totalProductToday() {
        List<BillDetail> billDetails = billDetailService.findByCreateDate(LocalDate.now());
        return billDetails.stream()
                .filter(bd -> bd.getStatusBill() == BillStatus.THANH_CONG)
                .mapToInt(BillDetail::getQuantity)
                .sum();
    }


    @Override
    public BigDecimal totalRevenueToday() {
        List<Bill> totalBill = billService.findByCreateDate(LocalDate.now());
        return totalBill.stream()
                .filter(b -> b.getStatus() == BillStatus.THANH_CONG)
                .map(b -> {
                    BigDecimal shipCost = b.getMoneyShip() != null ? b.getMoneyShip() : BigDecimal.ZERO;
                    BigDecimal discount = b.getItemDiscount() != null ? b.getItemDiscount() : BigDecimal.ZERO;
                    return b.getTotalMoney().subtract(shipCost).subtract(discount);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Integer totalProductRefundToday() {
        List<BillDetail> billDetails = billDetailService.findByCreateDate(LocalDate.now());
        return billDetails.stream()
                .filter(bd -> bd.getStatusBill() == BillStatus.TRA_HANG)
                .mapToInt(BillDetail::getQuantity)
                .sum();
    }

    @Override
    public Integer totalProductCanelToday() {
        List<BillDetail> bills = billDetailService.findByCreateDate(LocalDate.now());
        return bills.stream()
                .filter(bd -> bd.getStatusBill() == BillStatus.DA_HUY)
                .mapToInt(BillDetail::getQuantity)
                .sum();
    }

    @Override
    public BigDecimal totalRevenueDate(LocalDate date) {
        List<Bill> totalBill = billService.findByCreateDate(date);
        return totalBill.stream()
                .filter(b -> b.getStatus() == BillStatus.THANH_CONG)
                .map(b -> {
                    BigDecimal shipCost = b.getMoneyShip() != null ? b.getMoneyShip() : BigDecimal.ZERO;
                    BigDecimal discount = b.getItemDiscount() != null ? b.getItemDiscount() : BigDecimal.ZERO;
                    return b.getTotalMoney().subtract(shipCost).subtract(discount);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Integer totalProductDate(LocalDate date) {
        List<BillDetail> billDetails = billDetailService.findByCreateDate(date);
        return billDetails.stream()
                .filter(bd -> bd.getStatusBill() == BillStatus.THANH_CONG)
                .mapToInt(BillDetail::getQuantity)
                .sum();
    }

    @Override
    public Integer totalProductRefundDate(LocalDate date) {
        List<BillDetail> billDetails = billDetailService.findByCreateDate(date);
        return billDetails.stream()
                .filter(bd -> bd.getStatusBill() == BillStatus.TRA_HANG)
                .mapToInt(BillDetail::getQuantity)
                .sum();
    }

    @Override
    public Integer totalProductCanelDate(LocalDate date) {
        List<BillDetail> bills = billDetailService.findByCreateDate(date);
        return bills.stream()
                .filter(bd -> bd.getStatusBill() == BillStatus.DA_HUY)
                .mapToInt(BillDetail::getQuantity)
                .sum();
    }

    @Override
    public BigDecimal totalRevenueYear(Integer year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = Year.of(year).atMonth(12).atEndOfMonth();

        List<Bill> bills = billService.findByCreateDateBetween(startDate, endDate);
        return bills.stream()
                .filter(b -> b.getStatus() == BillStatus.THANH_CONG)
                .map(b -> {
                    BigDecimal shipCost = b.getMoneyShip() != null ? b.getMoneyShip() : BigDecimal.ZERO;
                    BigDecimal discount = b.getItemDiscount() != null ? b.getItemDiscount() : BigDecimal.ZERO;
                    return b.getTotalMoney().subtract(shipCost).subtract(discount);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Integer totalProductYear(Integer year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = Year.of(year).atMonth(12).atEndOfMonth();

        List<BillDetail> billDetails = billDetailService.findByCreateDateBetween(startDate, endDate);
        return billDetails.stream()
                .filter(bd -> bd.getStatusBill() == BillStatus.THANH_CONG)
                .mapToInt(BillDetail::getQuantity)
                .sum();
    }

    @Override
    public Integer totalProductRefundYear(Integer year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = Year.of(year).atMonth(12).atEndOfMonth();

        List<BillDetail> billDetails = billDetailService.findByCreateDateBetween(startDate, endDate);
        return billDetails.stream()
                .filter(bd -> bd.getStatusBill() == BillStatus.TRA_HANG)
                .mapToInt(BillDetail::getQuantity)
                .sum();
    }

    @Override
    public Integer totalProductCanelYear(Integer year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = Year.of(year).atMonth(12).atEndOfMonth();
        List<BillDetail> bills = billDetailService.findByCreateDateBetween(startDate, endDate);
        return bills.stream()
                .filter(bd -> bd.getStatusBill() == BillStatus.DA_HUY)
                .mapToInt(BillDetail::getQuantity)
                .sum();
    }

    @Override
    public BigDecimal totalRevenueMonthAndYear(int month, int year) {
        LocalDate startDate = YearMonth.of(year, month).atDay(1);
        LocalDate endDate = YearMonth.of(year, month).atEndOfMonth();


        List<Bill> bills = billService.findByCreateDateBetween(startDate, endDate);
        return bills.stream()
                .filter(b -> b.getStatus() == BillStatus.THANH_CONG)
                .map(Bill::getTotalMoney)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Integer totalProductMonthAndYear(int month, int year) {
        LocalDate startDate = YearMonth.of(year, month).atDay(1);
        LocalDate endDate = YearMonth.of(year, month).atEndOfMonth();


        List<BillDetail> billDetails = billDetailService.findByCreateDateBetween(startDate, endDate);
        return billDetails.stream()
                .filter(bd -> bd.getStatusBill() == BillStatus.THANH_CONG)
                .mapToInt(BillDetail::getQuantity)
                .sum();

    }

    @Override
    public Integer totalProductRefundMonthAndYear(int month, int year) {
        LocalDate startDate = YearMonth.of(year, month).atDay(1);
        LocalDate endDate = YearMonth.of(year, month).atEndOfMonth();

        List<BillDetail> billDetails = billDetailService.findByCreateDateBetween(startDate, endDate);
        return billDetails.stream()
                .filter(bd -> bd.getStatusBill() == BillStatus.TRA_HANG)
                .mapToInt(BillDetail::getQuantity)
                .sum();
    }


    @Override
    public List<ProductDetail> getBestSellingProducts() {
        List<BillDetail> billDetails = billDetailService.findAll();
        return billDetails.stream()
                .filter(detail -> detail.getStatusBill() == BillStatus.THANH_CONG) // Lọc đơn thành công
                .collect(Collectors.groupingBy(
                        BillDetail::getProductDetail,
                        Collectors.summingInt(BillDetail::getQuantity)
                ))

                .entrySet().stream()
                .sorted(Map.Entry.<ProductDetail, Integer>comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .limit(3)
                .collect(Collectors.toList());
    }

    @Override
    public List<Map.Entry<ProductDetail, Integer>> getBestSellingProduct() {
        List<BillDetail> billDetails = billDetailService.findAll();

        return billDetails.stream()
                .filter(detail -> detail.getStatusBill() == BillStatus.THANH_CONG) // Lọc đơn thành công
                .collect(Collectors.groupingBy(
                        BillDetail::getProductDetail,
                        Collectors.summingInt(BillDetail::getQuantity)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<ProductDetail, Integer>comparingByValue(Comparator.reverseOrder()))
                .limit(3)
                .collect(Collectors.toList());
    }




}
