package org.fpoly.capstone.service.payload.user;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
@Service
public class FileUploadImagesService {

    private static final String AVATAR_FOLDER = "src/main/resources/static/assets/images/avatar/";

    // Danh sách định dạng file hợp lệ
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "webp");

    public String saveAvatar(MultipartFile file, String userType, Long userId) {
        if (file.isEmpty()) {
            return null;
        }

        try {
            // Lấy tên file gốc và phần mở rộng
            String originalFileName = file.getOriginalFilename();
            assert originalFileName != null;
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();

            // Kiểm tra định dạng file
            if (!ALLOWED_EXTENSIONS.contains(fileExtension)) {
                throw new IllegalArgumentException("Chỉ hỗ trợ các định dạng ảnh: " + ALLOWED_EXTENSIONS);
            }

            // Tạo thư mục nếu chưa tồn tại
            File folder = new File(AVATAR_FOLDER);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // 🔹 Tạo UUID ngắn (8 ký tự đầu)
            String uniqueId = UUID.randomUUID().toString().substring(0, 8);

            // 🔹 Định dạng tên file: employee_1_abc12345.jpg
            String fileName = userType + "_" + userId + "_" + uniqueId + "." + fileExtension;
            File avatarFile = new File(AVATAR_FOLDER + fileName);

            // Copy file vào thư mục
            Files.copy(file.getInputStream(), avatarFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Trả về đường dẫn để lưu vào database
            return "/assets/images/avatar/" + fileName;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

//    public String saveAvatar(MultipartFile file, String userType, Long userId) {
//        if (file.isEmpty()) {
//            return null;
//        }
//
//        try {
//            // Lấy tên file gốc và phần mở rộng
//            String originalFileName = file.getOriginalFilename();
//            assert originalFileName != null;
//            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();
//
//            // Kiểm tra định dạng file
//            if (!ALLOWED_EXTENSIONS.contains(fileExtension)) {
//                throw new IllegalArgumentException("Chỉ hỗ trợ các định dạng ảnh: " + ALLOWED_EXTENSIONS);
//            }
//
//            // Tạo thư mục nếu chưa tồn tại
//            File folder = new File(AVATAR_FOLDER);
//            if (!folder.exists()) {
//                folder.mkdirs();
//            }
//
//            // Định dạng tên file: employee_1.jpg hoặc customer_2.png
//            String fileName = userType + "_" + userId + "." + fileExtension;
//            File avatarFile = new File(AVATAR_FOLDER + fileName);
//
//            // Copy file vào thư mục
//            Files.copy(file.getInputStream(), avatarFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//
//            // Trả về đường dẫn để lưu vào database
//            return "/assets/images/avatar/" + fileName;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

