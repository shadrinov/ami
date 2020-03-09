package ru.ntechs.ami.actions;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.ntechs.ami.AMI;

@Getter
@Setter
@ToString
public class Originate extends Action {
	private String channel;
	private String exten;
	private String context;
	private String priority;
	private String application;
	private String data;
	private String timeout;
	private String callerId;
	private String variable;
	private String account;
	private String earlyMedia;
	private String async;
	private String codecs;
	private String channelId;
	private String otherChannelId;

	public Originate(AMI ami) {
		super(ami, "Originate");
	}

	@Override
	public ArrayList<String> getMessageText() {
		ArrayList<String> request = super.getMessageText();

		if (channel != null)
			request.add(String.format("Channel: %s", channel));

		if (exten != null)
			request.add(String.format("Exten: %s", exten));

		if (context != null)
			request.add(String.format("Context: %s", context));

		if (priority != null)
			request.add(String.format("Priority: %s", priority));

		if (application != null)
			request.add(String.format("Application: %s", application));

		if (data != null)
			request.add(String.format("Data: %s", data));

		if (timeout != null)
			request.add(String.format("Timeout: %s", timeout));

		if (callerId != null)
			request.add(String.format("CallerID: %s", callerId));

		if (variable != null)
			request.add(String.format("Variable: %s", variable));

		if (account != null)
			request.add(String.format("Account: %s", account));

		if (earlyMedia != null)
			request.add(String.format("EarlyMedia: %s", earlyMedia));

		if (async != null)
			request.add(String.format("Async: %s", async));

		if (codecs != null)
			request.add(String.format("Codecs: %s", codecs));

		if (channelId != null)
			request.add(String.format("ChannelId: %s", channelId));

		if (otherChannelId != null)
			request.add(String.format("OtherChannelId: %s", otherChannelId));

		return request;
	}
}
