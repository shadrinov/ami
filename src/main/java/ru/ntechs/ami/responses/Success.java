package ru.ntechs.ami.responses;

import ru.ntechs.ami.AMI;
import ru.ntechs.ami.Response;

public class Success extends Response {
	private String message;

	public Success(AMI ami, String name) {
		super(ami, name);
	}

	@Override
	protected void engage(String attr, String value) {
		super.engage(attr, value);

		if (attr.equalsIgnoreCase("Message"))
			message = value;
		else
			warnUnsupportedAttr(attr, value);
	}

	public String getMessage() {
		return message;
	}
}
