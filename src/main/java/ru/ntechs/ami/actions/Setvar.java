package ru.ntechs.ami.actions;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.ntechs.ami.AMI;

@Getter
@Setter
@ToString
public class Setvar extends Action {
	String channel;
	String variable;
	String value;

	public Setvar(AMI ami) {
		super(ami, "Setvar");
	}

	@Override
	public ArrayList<String> getMessageText() {
		ArrayList<String> request = super.getMessageText();

		if (channel != null)
			request.add(String.format("Channel: %s", channel));

		if (variable != null)
			request.add(String.format("Variable: %s", variable));

		if (value != null)
			request.add(String.format("Value: %s", value));

		return request;
	}

}
