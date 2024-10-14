# Sử dụng image Java 11 làm base image
FROM openjdk:21-jdk as build

# Thiết lập working directory trong container
WORKDIR /app

# Sao chép file .m2 từ máy host vào container để tăng tốc độ build
# Bước này là tùy chọn, nếu bạn muốn cache maven dependencies
# COPY path/to/your/.m2 /root/.m2

# Sao chép dự án vào working directory
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
COPY uploads uploads

# Build ứng dụng Spring Boot sử dụng Maven
RUN ./mvnw package -DskipTests

# Chuyển sang giai đoạn mới để giảm kích thước của image
FROM openjdk:21-jre-slim

WORKDIR /app

# Sao chép file .jar từ giai đoạn build sang giai đoạn này
COPY --from=build /app/target/*.jar app.jar

# Cài đặt curl (tùy chọn, có thể hữu ích cho việc kiểm tra sức khỏe của ứng dụng)
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Khai báo port mà ứng dụng sử dụng
EXPOSE 8080

# Chạy ứng dụng Spring Boot
CMD ["java", "-jar", "app.jar"]