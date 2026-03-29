package egate.digital.fasotour.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final FileStorageConfig config;

    public WebConfig(FileStorageConfig config) {
        this.config = config;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String uploadPath = config.getUploadDir()
                .toUri()
                .toString();

        registry.addResourceHandler("/api/fasotour/uploads/**")
                .addResourceLocations(uploadPath);
    }
}