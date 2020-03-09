package ru.ntechs.ami.events;

import ru.ntechs.ami.AMI;

public class PeerStatus extends Event {
	private String privilege;
	private String channelType;
	private String peer;
	private String peerStatus;
	private String address;
	private Integer time;
	private String cause;

	public PeerStatus(AMI ami, String name) {
		super(ami, name);
	}

	@Override
	protected boolean engage(String attr, String value) {
		if (super.engage(attr, value))
			return true;

		if (attr.equalsIgnoreCase("Privilege"))
			privilege = value;
		else if (attr.equalsIgnoreCase("ChannelType"))
			channelType = value;
		else if (attr.equalsIgnoreCase("Peer"))
			peer = value;
		else if (attr.equalsIgnoreCase("PeerStatus"))
			peerStatus = value;
		else if (attr.equalsIgnoreCase("Address"))
			address = value;
		else if (attr.equalsIgnoreCase("Time"))
			time = Integer.decode(value);
		else if (attr.equalsIgnoreCase("Cause"))
			cause = value;
		else {
			warnUnsupportedAttr(attr, value);
			return false;
		}

		return true;
	}

	public String getPrivilege() {
		return privilege;
	}

	public String getChannelType() {
		return channelType;
	}

	public String getPeer() {
		return peer;
	}

	public String getPeerStatus() {
		return peerStatus;
	}

	public String getAddress() {
		return address;
	}

	public Integer getTime() {
		return time;
	}

	public String getCause() {
		return cause;
	}
}
