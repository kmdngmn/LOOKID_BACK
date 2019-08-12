package com.java.common.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.java.common.interceptor.JwtInterceptor;

/*
 * Interceptor�� ����ϱ� ���ؼ� @Configuration �� WebMvcConfigurer �� ������ Ŭ���� ����.
 * addInterceptors()�� JwtInterceptor�� ����ϰ�, ����ϰų� ������ URI�� �ۼ�. �Ϲ������δ� ��ū�� �ʿ����� ���� ��츦 ����.
 * 
 */

//@Configuration
public class WebConfig implements WebMvcConfigurer {

	private static final String[] EXCLUDE_PATHS = { // ������ URI �ۼ�
				
			"/error/**" 
	
	}; 

	@Autowired
	private JwtInterceptor jwtInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

//		registry.addInterceptor(jwtInterceptor)
//						.addPathPatterns("/**")
//						.excludePathPatterns(EXCLUDE_PATHS);

	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		// TODO Auto-generated method stub

	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		// TODO Auto-generated method stub

	}

	@Override
	public Validator getValidator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
		// TODO Auto-generated method stub

	}

	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub

	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		// TODO Auto-generated method stub

	}

}
