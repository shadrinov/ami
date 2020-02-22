package ru.ntechs.ami;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Action extends Message {

	public Action(AMI ami, String name) {
		super(ami, "Action", name);
	}

	public abstract Iterable<String> getMessageText();

	@Override
	protected void engage(String attr, String value) {
		super.engage(attr, value);
		log.warn(String.format("Useless use of method 'engage': attribute \"%s\" for message of type \"%s\". Value: \"%s\"", attr, getType(), value));
		return;
	}

	public void submit() {
		getAMI().submit(this);
	}
}
