package ru.ntechs.ami;

public class PlainMessage extends Message {

	public PlainMessage(AMI ami, String type, String name) {
		super(ami, type, name);

//		logger.warn(String.format("Unsupported message \"%s: %s\"", type, name));
	}
}
