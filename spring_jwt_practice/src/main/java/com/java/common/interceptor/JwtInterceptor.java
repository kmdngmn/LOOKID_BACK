package com.java.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.java.common.exception.UnauthorizedException;
import com.java.common.service.JwtService;

/*
 * HTTP ��� �� ���ͼ��͸� Ȱ���Ͽ� ��ȿ�� ��ū���� Ȯ���ϴ� �ڵ�. ��ȿ���˻�.
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

	private static final String HEADER_AUTH = "Authorization";

	@Autowired
	private JwtService jwtService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		final String token = request.getHeader(HEADER_AUTH); // HTTP ����� ��� ��ū�� ������. (��û)

		if (token != null && jwtService.isUsable(token)) {
			return true; //��ū�� �����ϸ� ��ȿ�� �� true�� ����
		} else {
			throw new UnauthorizedException(); // �׷��� ������ ���ܹ߻�
		}
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
