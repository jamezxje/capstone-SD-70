package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.BillDetail;
import org.fpoly.capstone.entity.ProductDetail;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public interface RevenueService {

    BigDecimal totalRevenue();

    Integer totalProduct();

    Integer totalProductRefund();

    Integer totalProductCanel();

    Integer totalProductToday();

    BigDecimal totalRevenueToday();

    Integer totalProductRefundToday();

    Integer totalProductCanelToday();

    List<Map.Entry<ProductDetail, Integer>> getBestSellingProductToday();

    BigDecimal totalRevenueDate(LocalDate date);

    Integer totalProductDate(LocalDate date);

    Integer totalProductRefundDate(LocalDate date);

    Integer totalProductCanelDate(LocalDate date);

    List<Map.Entry<ProductDetail, Integer>> getBestSellingProductDate(LocalDate date);

    BigDecimal totalRevenueYear(Integer year);

    Integer totalProductYear(Integer year);

    Integer totalProductRefundYear(Integer year);

    Integer totalProductCanelYear(Integer year);

    List<Map.Entry<ProductDetail, Integer>> getBestSellingProductYears(Integer year);

    Integer totalProductCanelMonthAndYear(int month, int year);

    BigDecimal totalRevenueMonthAndYear(int month, int year);

    Integer totalProductMonthAndYear(int month, int year);

    Integer totalProductRefundMonthAndYear(int month, int year);

    List<Map.Entry<ProductDetail, Integer>> getBestSellingProductMonthAndYear(int month, int year);

    List<ProductDetail> getBestSellingProducts();

    List<Map.Entry<ProductDetail, Integer>> getBestSellingProduct();
}
