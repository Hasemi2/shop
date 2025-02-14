package com.practice.shop;

import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule.Feature;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ShopApplication {

  public static void main(String[] args) {
    SpringApplication.run(ShopApplication.class, args);
  }

//  @Bean
//  Hibernate5JakartaModule hibernate5JakartaModule() {
//    Hibernate5JakartaModule hibernate5JakartaModule = new Hibernate5JakartaModule();
//    hibernate5JakartaModule.configure(Feature.FORCE_LAZY_LOADING, true);
//    return hibernate5JakartaModule;
//  }
}
