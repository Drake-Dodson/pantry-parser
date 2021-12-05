package com.example.pantryparserbackend.Services;

import com.example.pantryparserbackend.Permissions.IP;
import com.example.pantryparserbackend.Permissions.IPRepository;
import com.example.pantryparserbackend.Users.User;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Service
public class IPService {
	@Autowired
	IPRepository ipRepository;

	private final String LOCALHOST_IPV4 = "127.0.0.1";
	private final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

	public String getClientIp(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-Forwarded-For");
		if(StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}

		if(StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}

		if(StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if(LOCALHOST_IPV4.equals(ipAddress) || LOCALHOST_IPV6.equals(ipAddress)) {
				try {
					InetAddress inetAddress = InetAddress.getLocalHost();
					ipAddress = inetAddress.getHostAddress();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
		}

		if(!StringUtils.isEmpty(ipAddress) && ipAddress.length() > 15 && ipAddress.indexOf(",") > 0) {
			ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
		}

		return ipAddress;
	}

	public void saveIP(User user, HttpServletRequest request) {
		this.cleanOldIPs(user);
		String address = this.getClientIp(request);
		IP ip = new IP(user, address);
		ipRepository.deleteByIP(address);
		ipRepository.save(ip);
	}

	public User getCurrentUser(HttpServletRequest request) {
		IP ip = ipRepository.findByIP(this.getClientIp(request));
		if(ip == null) {
			return null;
		}
		if(ip.outOfDate()) {
			ipRepository.delete(ip);
			return null;
		}
		return ip.getUser();
	}

	public void cleanOldIPs(User user) {
		List<IP> ips = ipRepository.findByUser(user);
		for(IP ip : ips){
			if(ip.outOfDate()){
				ipRepository.delete(ip);
			}
		}
	}
}
