package ru.ntechs.ami.responses;

import lombok.ToString;
import ru.ntechs.ami.AMI;

@ToString(callSuper = true)
public class Error extends Response {
	private String message;

	public Error(AMI ami, String name) {
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

	public String getMessage() {
		return message;
	}

}
