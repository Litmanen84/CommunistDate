package com.example.CommunistDate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {

    //  @Value("${CLOUDINARY_CLOUD_NAME}")
    // private String cloudName;

    // @Value("${CLOUDINARY_API_KEY}")
    // private String apiKey;

    // @Value("${CLOUDINARY_API_SECRET}")
    // private String apiSecret;

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
