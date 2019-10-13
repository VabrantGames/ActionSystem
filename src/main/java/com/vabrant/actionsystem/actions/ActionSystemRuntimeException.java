package com.vabrant.actionsystem.actions;

public class ActionSystemRuntimeException extends RuntimeException {
	
private static final long serialVersionUID = -7980202882003567189L;
	
	public ActionSystemRuntimeException(String message) {
		super(message);
	}
	
	public ActionSystemRuntimeException(Throwable cause) {
		super(cause);
	}
	
	public ActionSystemRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

}
