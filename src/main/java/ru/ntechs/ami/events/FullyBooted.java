package ru.ntechs.ami.events;

import ru.ntechs.ami.AMI;

public class FullyBooted extends Event {
	private String privilege;

	public FullyBooted(AMI ami, String name) {
		super(ami, name);
	}

	@Override
	protected boolean engage(String attr, String value) {
		if (super.engage(attr, value))
			return true;

		if (attr.equalsIgnoreCase("Privilege"))
			privilege = value;
		else if (attr.equalsIgnoreCase("Status")) {}
		else {
			warnUnsupportedAttr(attr, value);
			return false;
		}

		return true;
	}

	public String getPrivilege() {
		return privilege;
	}
}
