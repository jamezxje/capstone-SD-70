package org.fpoly.capstone.service.common.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.entity.Image;
import org.fpoly.capstone.entity.type.ImageFormat;
import org.fpoly.capstone.repository.ImageRepository;
import org.fpoly.capstone.service.common.FilesStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class FilesStorageServiceImpl implements FilesStorageService {

  private final ImageRepository imageRepository;
  private final Path root = Paths.get("uploads");

  @PostConstruct
  public void init() {
    try {
      Files.createDirectories(this.root);
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize folder for upload!");
    }
  }

  @Override
  public Image save(MultipartFile file) {
    Image image = this.parseInfo(file);

    if (image == null) {
      return null;
    }

    try {
      Files.copy(file.getInputStream(), Path.of(image.getUrl()));

      this.imageRepository.save(image);

      if (image.getId() == null) {
        return null;
      }

      return image;
    } catch (Exception e) {
      return null;
    }
  }


//  @Override
//  public List<Image> saveAll(MultipartFile[] files) {
//
//    List<Image> images = new ArrayList<>();
//
//    try {
//      for (MultipartFile file : files) {
//        if (file.isEmpty()) {
//          continue;
//        }
//        Image image = this.parseInfo(file);
//        if (image == null) {
//          continue;
//        }
//        Files.copy(file.getInputStream(), Path.of(image.getUrl()));
//        images.add(image);
//      }
//
//      List<Image> savedImages = this.imageRepository.saveAll(images);
//
//      if (savedImages.size() == images.size()) {
//        return savedImages;
//      } else {
//        for (Image image : images) {
//          Files.deleteIfExists(Path.of(image.getUrl()));
//        }
//
//        return null;
//      }
//
//    } catch (Exception e) {
//      e.printStackTrace();
//
//      if (!images.isEmpty()) {
//
//        try {
//          for (Image image : images) {
//            Files.deleteIfExists(Path.of(image.getUrl()));
//          }
//        } catch (IOException ioException) {
//          ioException.printStackTrace();
//        }
//
//      }
//
//      return null;
//    }
//  }

  @Override
  public List<Image> saveAll(MultipartFile[] files) {
    List<Image> images = this.processFiles(files);
    if (images.isEmpty()) {
      return null;
    }

    try {
      List<Image> savedImages = this.imageRepository.saveAll(images);
      if (savedImages.size() == images.size()) {
        return savedImages;
      } else {
        this.cleanupFiles(images);
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
      this.cleanupFiles(images);
      return null;
    }
  }

  private List<Image> processFiles(MultipartFile[] files) {
    List<Image> images = new ArrayList<>();
    for (MultipartFile file : files) {
      if (!file.isEmpty()) {
        Image image = this.parseInfo(file);
        if (image != null) {
          try {
            Files.copy(file.getInputStream(), Path.of(image.getUrl()));
            images.add(image);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return images;
  }

  private void cleanupFiles(List<Image> images) {
    for (Image image : images) {
      try {
        Files.deleteIfExists(Path.of(image.getUrl()));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }


  @Override
  public void deleteAll(List<Image> images) {
    try {
      for (Image image : images) {
        Files.deleteIfExists(Path.of(image.getUrl()));
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void delete(Image image) {
    try {
      Files.deleteIfExists(Path.of(image.getUrl()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Image parseInfo(MultipartFile file) {

    if (file.isEmpty()) {
      return null;
    }

    String prefix = UUID.randomUUID().toString();
    String fileDestinationName = prefix.concat("-").concat(convertToSlug(file.getOriginalFilename()));

    ImageFormat imageFormat = ImageFormat.JPG;

    switch (file.getContentType().toLowerCase()) {
      case "image/png":
        imageFormat = ImageFormat.PNG;
        break;
      case "image/jpeg":
        imageFormat = ImageFormat.JPEG;
        break;
      case "image/jpg":
        imageFormat = ImageFormat.JPG;
        break;
      case "image/webp":
        imageFormat = ImageFormat.WEBP;
        break;
      default:
        // not allowed
        return null;
    }

    Image image = new Image(
        file.getOriginalFilename(), imageFormat,
        this.root.resolve(fileDestinationName).toString().replace("uploads\\", "uploads/"),
        extractFileNameWithoutExtension(file.getOriginalFilename()));
    
    return image;
  }

  public static String extractFileNameWithoutExtension(String fileName) {
    if (fileName == null) {
      return null;
    }

    int lastDotIndex = fileName.lastIndexOf('.');
    if (lastDotIndex == -1) {
      return fileName;
    }

    return fileName.substring(0, lastDotIndex);
  }

  public static String convertToSlug(String fileName) {
    if (fileName == null) {
      return null;
    }

    Pattern pattern = Pattern.compile("^(.*?)(\\.[^.]+)?$");
    Matcher matcher = pattern.matcher(fileName);
    if (!matcher.matches()) {
      return fileName;
    }

    String name = matcher.group(1);
    String extension = matcher.group(2);

    String slug = name.toLowerCase().replaceAll("\\s", "-");

    if (extension != null) {
      slug += extension;
    }

    return slug;
  }
}
