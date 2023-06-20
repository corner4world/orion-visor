package com.orion.ops.framework.web.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.common.enums.FilterOrderConst;
import com.orion.ops.framework.web.core.filter.TraceIdFilter;
import com.orion.ops.framework.web.core.handler.GlobalExceptionHandler;
import com.orion.ops.framework.web.core.handler.WrapperResultHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;

/**
 * web 配置类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/16 16:26
 */
@AutoConfiguration
public class OrionWebAutoConfiguration implements WebMvcConfigurer {

    @Value("${orion.api.prefix}")
    private String orionApiPrefix;

    // TODO XSS

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // 公共 api 前缀
        configurer.addPathPrefix(orionApiPrefix, clazz -> clazz.isAnnotationPresent(RestController.class));
    }

    /**
     * @return 全局异常处理器
     */
    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    /**
     * @return 通用返回结果处理器
     */
    @Bean
    public WrapperResultHandler wrapperResultHandler() {
        return new WrapperResultHandler();
    }

    /**
     * @return http json 转换器
     */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        // 转换器
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        // 配置
        FastJsonConfig config = new FastJsonConfig();
        // 支持的类型
        List<MediaType> mediaTypes = Lists.of(
                MediaType.APPLICATION_JSON,
                MediaType.APPLICATION_FORM_URLENCODED,
                MediaType.APPLICATION_XHTML_XML,
                MediaType.TEXT_PLAIN,
                MediaType.TEXT_HTML,
                MediaType.TEXT_XML
        );
        converter.setSupportedMediaTypes(mediaTypes);
        // 序列化配置
        config.setSerializerFeatures(
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.IgnoreNonFieldGetter
        );
        converter.setFastJsonConfig(config);
        return new HttpMessageConverters(converter);
    }

    /**
     * @return 跨域配置
     */
    @Bean
    @ConditionalOnProperty(value = "orion.api.cors", havingValue = "true")
    public FilterRegistrationBean<CorsFilter> corsFilterBean() {
        // 跨域配置
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setMaxAge(3600L);
        // 创建 UrlBasedCorsConfigurationSource 对象
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return createFilterBean(new CorsFilter(source), FilterOrderConst.CORS_FILTER);
    }

    /**
     * @return traceId 配置
     */
    @Bean
    public FilterRegistrationBean<TraceIdFilter> traceIdFilterBean() {
        return createFilterBean(new TraceIdFilter(), FilterOrderConst.TRICE_ID_FILTER);
    }

    /**
     * 创建过滤器
     *
     * @param filter filter
     * @param order  order
     * @param <T>    type
     * @return filter bean
     */
    public static <T extends Filter> FilterRegistrationBean<T> createFilterBean(T filter, Integer order) {
        FilterRegistrationBean<T> bean = new FilterRegistrationBean<>(filter);
        bean.setOrder(order);
        return bean;
    }

}
