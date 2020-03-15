package ru.ntechs.ami;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Message {
	private AMI ami;

	private String type;
	private String name;

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
		log.warn(String.format("%s: %s", getType(), getName()));

		for (String attr : keyOrder) {
			log.warn(String.format("%s: %s", attr, body.get(attr)));
		}

		log.warn("");
	}

	protected boolean engage(String attr, String value) {
		keyOrder.add(attr);
		body.put(attr, value);

		return false;
	}

	protected void warnUnsupportedAttr(String attr, String value) {
		log.warn(String.format("Unsupported attribute \"%s\" in message of type \"%s: %s\". Value: \"%s\"", attr, getType(), getName(), value));
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
