package com.github.elgleidson.multi.tenant.database.exception;

public class RoleAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public RoleAlreadyExistsException(String nome) {
		super(nome);
	}
	
}
