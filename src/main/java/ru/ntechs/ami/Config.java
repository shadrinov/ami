package ru.ntechs.ami;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix="ami")
public class Config {
	private String hostname = "127.0.0.1";
	private Integer port = 5038;
	private String username;
	private String password;

	public String getHostname() {
		return hostname;
	}

	public Integer getPort() {
		return port;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
