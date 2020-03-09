package ru.ntechs.ami.responses;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.ntechs.ami.AMI;
import ru.ntechs.ami.Message;

@Getter
@Setter
@ToString
public abstract class Response extends Message {
	private String actionId;

	public Response(AMI ami, String name) {
		super(ami, "Response", name);
	}

	@Override
	protected boolean engage(String attr, String value) {
		if (super.engage(attr, value))
			return true;

		if (attr.equalsIgnoreCase("ActionID"))
			actionId = value;
		else
			return false;

		return true;
	}
}
