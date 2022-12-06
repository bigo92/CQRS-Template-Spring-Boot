package com.cqrs.base;

import java.util.List;
import java.util.Map;

public class Response<T> {
    /**
	 * Request's result data
	 */
	public T data;
	/**
	 * Request's result exception (if there's any)
	 */
	public Map<String,List<String>> errors;

	/**
	 * Return if request has exception
	 * 
	 * @return true if exception occured; otherwise false
	 */
	public boolean isSucess() {
		return errors != null;
	}
}
