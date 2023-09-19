package com.highright.highcare.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {

    private final String[] CLASSPATH_RESOURCE_LOCATIONS = { "classpath:/META-INF/resources/",
            "classpath:/resources/", "classpath:/static/", "classpath:/public/" };

    // c드라이브 경로를 여기에 저장 yml에 설정해준 것이 여기로
    @Value("${image.add-resource-locations}")
    private String ADD_RESOURCE_LOCATION;

    @Value("${image.add-resource-handler}")
    private String ADD_RESOURCE_HANDLER;

    @Value("${file.add-resource-locations}")
    private String fileResourceLocations;

    @Value("${file.add-resource-handler}")
    private String fileResourceHandler;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        // 이미지 경로 매핑
        // ADD_RESOURCE_LOCATION이건
        // yml에서  add-resource-locations: file:///C:/dev/profileImages/ 에서 설정해주고 변수로 가져온것
        registry.addResourceHandler(ADD_RESOURCE_HANDLER)
                .addResourceLocations(ADD_RESOURCE_LOCATION);

        // 전자결재 파일 경로 매핑
        registry.addResourceHandler(fileResourceHandler)
                .addResourceLocations(fileResourceLocations);
    }



}
