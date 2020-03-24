package ru.ntechs.ami.responses;

import lombok.ToString;
import ru.ntechs.ami.AMI;

@ToString(callSuper = true)
public class Success extends Response {
	private String message;

	public Success(AMI ami, String name) {
		super(ami, name);
	}

	@Override
	protected boolean engage(String attr, String value) {
		if (super.engage(attr, value))
			return true;

		if (attr.equalsIgnoreCase("Message"))
			message = value;
		else {
			warnUnsupportedAttr(attr, value);
			return false;
		}

		return true;
	}

	@Override
	public boolean isSuccess() {
		return true;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
