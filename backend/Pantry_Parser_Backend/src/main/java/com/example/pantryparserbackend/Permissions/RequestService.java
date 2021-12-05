package com.example.pantryparserbackend.Permissions;

import javax.servlet.http.HttpServletRequest;

public interface RequestService {
	String getClientIp(HttpServletRequest request);
}
