package com.gtss.mnp_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MnpManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MnpManagerApplication.class, args);
    }

    // @Bean
    // WebMvcConfigurer corsConfigurer() {
    //     return new WebMvcConfigurer() {
    //         @Override
    //         public void addCorsMappings(CorsRegistry registry) {
    //             registry.addMapping("/**").allowedHeaders("*")
    //                     .allowedMethods("*").allowedOrigins("*");
    //         }
    //     };
    // }

}
