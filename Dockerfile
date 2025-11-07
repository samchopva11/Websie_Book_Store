# Sử dụng Java 25 chính thức từ Eclipse Temurin
FROM eclipse-temurin:25-jdk AS build

# Đặt thư mục làm việc
WORKDIR /app

# Copy toàn bộ mã nguồn và build ứng dụng
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Tạo image chạy chính
FROM eclipse-temurin:25-jre
WORKDIR /app

# Copy file jar từ giai đoạn build
COPY --from=build /app/target/bookstore-0.0.1-SNAPSHOT.jar app.jar

# Mở cổng
EXPOSE 8080
EXPOSE ${PORT}
# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
