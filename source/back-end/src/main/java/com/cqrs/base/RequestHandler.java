package com.cqrs.base;

public interface RequestHandler<T, R> {
	/**
	 * Handles a request
	 * 
	 * @param request Request parameter
	 * @return Result type
	 */
	public R handle(T request);
}