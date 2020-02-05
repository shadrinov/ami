package ru.ntechs.ami;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Message {
	private AMI ami;

	private final String type;
	private final String name;

	protected final Logger logger = LoggerFactory.getLogger(Message.class);

	private ArrayList<String> keyOrder = new ArrayList<>();
	private HashMap<String, String> body = new HashMap<>();

	public Message(AMI ami, String type, String name) {
		super();

		this.ami = ami;
		this.type = type;
		this.name = name;
	}

	public AMI getAMI() {
		return ami;
	}

	protected void setAMI(AMI ami) {
		this.ami = ami;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public void dump() {
		logger.warn(String.format("%s: %s", getType(), getName()));

		for (String attr : keyOrder) {
			logger.warn(String.format("%s: %s", attr, body.get(attr)));
		}

		logger.warn("");
	}

	protected void engage(String attr, String value) {
		keyOrder.add(attr);
		body.put(attr, value);
	}

	protected void warnUnsupportedAttr(String attr, String value) {
		logger.warn(String.format("Unsupported attribute \"%s\" for message of type \"%s: %s\". Value: \"%s\"", attr, getType(), getName(), value));
	}

	public ArrayList<String> getKeyOrder() {
		return keyOrder;
	}

	public HashMap<String, String> getBody() {
		return body;
	}

	public String getAttribute(String attrName) {
		return body.get(attrName);
	}
}
