package org.fpoly.capstone.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Log4j2
@Configuration
public class CommonBeanConfig {
  @Bean
  public ModelMapper modelMapper() {
    final Converter<String, LocalDate> toLocalDate = context -> StringUtils.isBlank(context.getSource()) ? null : LocalDate.parse(context.getSource());
    final Converter<String, LocalDateTime> toLocalDateTime = context -> StringUtils.isBlank(context.getSource()) ? null : LocalDateTime.parse(context.getSource());
    final ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    modelMapper.getConfiguration().setSkipNullEnabled(true);
    modelMapper.addConverter(toLocalDate);
    modelMapper.addConverter(toLocalDateTime);
    return modelMapper;
  }

  @Bean
  public ObjectMapper objectMapper() {
    final ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    return objectMapper;
  }

}
