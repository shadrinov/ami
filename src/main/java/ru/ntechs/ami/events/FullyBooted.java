package ru.ntechs.ami.events;

import ru.ntechs.ami.AMI;
import ru.ntechs.ami.Event;

public class FullyBooted extends Event {
	private String privilege;

	public FullyBooted(AMI ami, String name) {
		super(ami, name);
	}

	@Override
	protected void engage(String attr, String value) {
		super.engage(attr, value);

		if (attr.equalsIgnoreCase("Privilege"))
			privilege = value;
		else if (attr.equalsIgnoreCase("Status")) {}
		else
			warnUnsupportedAttr(attr, value);
	}

	public String getPrivilege() {
		return privilege;
	}
}
