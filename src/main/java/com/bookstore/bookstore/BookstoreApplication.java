package com.bookstore.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
        System.out.println("=================================");
        System.out.println("✅ BookStore Application Started!");
        System.out.println("🌐 Open: http://localhost:8080");
        System.out.println("=================================");
    }
}
