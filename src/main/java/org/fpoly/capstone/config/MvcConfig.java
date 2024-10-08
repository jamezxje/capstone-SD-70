//package org.fpoly.capstone.config;
//
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@RequiredArgsConstructor
//public class MvcConfig implements WebMvcConfigurer {
//
//  private final FrontDataInterceptor frontDataInterceptor;
//  private final AdminDataInterceptor adminDataInterceptor;
//
//  @Override
//  public void addInterceptors(InterceptorRegistry registry) {
//    registry.addInterceptor(this.frontDataInterceptor)
//        .addPathPatterns("/**")
//        .excludePathPatterns("/dashboard/**");
//
//    registry.addInterceptor(this.adminDataInterceptor)
//        .addPathPatterns("/dashboard/**");
//  }
//
//  @Override
//  public void addResourceHandlers(ResourceHandlerRegistry registry) {
//    registry.addResourceHandler("/uploads/**").addResourceLocations("file:uploads/");
//  }
//
//}