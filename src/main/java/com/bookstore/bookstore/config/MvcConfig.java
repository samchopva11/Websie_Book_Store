package com.bookstore.bookstore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get("book-uploads");
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        // Cấu hình để trình duyệt có thể truy cập file trong thư mục book-uploads
        // qua đường dẫn /book-uploads/**
        registry.addResourceHandler("/book-uploads/**")
                .addResourceLocations("file:/" + uploadPath + "/");
    }
}
    