package com.github.elgleidson.multi.tenant.database.exception;

public class TenantNotExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public TenantNotExistsException(String nome) {
		super(nome);
	}
	
}
