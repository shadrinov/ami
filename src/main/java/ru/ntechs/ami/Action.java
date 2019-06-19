package ru.ntechs.ami;

public abstract class Action extends Message {

	public Action(AMI ami, String name) {
		super(ami, "Action", name);
	}

	public abstract Iterable<String> getMessageText();

	@Override
	protected void engage(String attr, String value) {
		super.engage(attr, value);
		logger.warn(String.format("Useless use of method 'engage': attribute \"%s\" for message of type \"%s\". Value: \"%s\"", attr, getType(), value));
		return;
	}
}
