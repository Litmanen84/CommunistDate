package com.example.CommunistDate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {


    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dfmlqauhg",
                "api_key", "297677327898944",
                "api_secret", "u_tKrGq45Tx4PlqOPBVNQdtveWg",
                "secure", true,
                "cdn_subdomain", true));
    }
}
