package com.example.pantryparserbackend.Permissions;

import com.example.pantryparserbackend.Users.User;
import lombok.Getter;

import javax.persistence.*;
import java.util.Date;

@Entity
public class IP {
	@Id
	@Getter
	@Column(nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Getter
	@ManyToOne
	private User user;

	@Getter
	@Column(nullable = false, unique = true)
	private String ip;

	@Getter
	@Column(nullable = false)
	private Date created_date;

	public IP (User user, String ip) {
		this.user = user;
		this.ip = ip;
		this.created_date = new Date();
	}

	public IP() {
		this.created_date = new Date();
	}

	/**
	 * Checks if an IP has been stored for more than 24 hours
	 * @return true or false
	 */
	public boolean outOfDate() {
		return this.created_date.after(new Date(this.created_date.getTime() + (24 * 60 * 60 * 1000)));
	}
}
