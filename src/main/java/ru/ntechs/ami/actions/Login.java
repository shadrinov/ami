package ru.ntechs.ami.actions;

import java.util.ArrayList;

import ru.ntechs.ami.AMI;
import ru.ntechs.ami.Action;

public class Login extends Action {
	private String login;
	private String password;

	public Login(AMI ami, String login, String password) {
		super(ami, "Login");

		this.login = login;
		this.password = password;
	}

	@Override
	public Iterable<String> getMessageText() {
		ArrayList<String> request = new ArrayList<>();

		request.add(String.format("Action: %s", getName()));
		request.add(String.format("Username: %s", login));
		request.add(String.format("Secret: %s", password));

		return request;
	}
}
