package org.fpoly.capstone.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageFormat {

  PNG("png"),
  WEBP("webp"),
  JPG("jpg"),
  JPEG("jpeg");

  private final String imageFormat;

}
