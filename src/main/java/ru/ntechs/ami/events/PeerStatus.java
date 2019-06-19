package ru.ntechs.ami.events;

import ru.ntechs.ami.AMI;
import ru.ntechs.ami.Event;

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
	protected void engage(String attr, String value) {
		super.engage(attr, value);

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
		else
			warnUnsupportedAttr(attr, value);
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
