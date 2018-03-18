package com.shbs.admin.spring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;
import org.springframework.web.servlet.resource.VersionResourceResolver;

import java.util.concurrent.TimeUnit;

@Configuration
public class StaticFilesConfiguration extends WebMvcConfigurerAdapter {

    private static final String STATIC_RESOURCES_LOCATION = "classpath:/static/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        VersionResourceResolver versionResourceResolver = new VersionResourceResolver()
            .addContentVersionStrategy("/**");
        registry
            .addResourceHandler("/**")
            .addResourceLocations(STATIC_RESOURCES_LOCATION)
            .setCacheControl(CacheControl.maxAge(1L, TimeUnit.DAYS))
            .resourceChain(true)
            .addResolver(versionResourceResolver);
    }

    @Bean
    public ResourceUrlEncodingFilter resourceUrlEncodingFilter() {
        return new ResourceUrlEncodingFilter();
    }
}
