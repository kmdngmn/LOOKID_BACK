package com.java.common.exception;

public class UnauthorizedException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnauthorizedException() {
		super("��ū�� ��ȿ���� �ʽ��ϴ�.(���� ���� invalid)\n�ٽ� �α��� ���ּ���.\n");
	}
	
}
