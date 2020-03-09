package ru.ntechs.ami.actions;

import java.util.ArrayList;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.ntechs.ami.AMI;
import ru.ntechs.ami.Message;

@Slf4j
@Getter
@Setter
public abstract class Action extends Message {
	private String actionId;

	public Action(AMI ami, String name) {
		super(ami, "Action", name);

		actionId = UUID.randomUUID().toString();
	}

	public ArrayList<String> getMessageText() {
		ArrayList<String> request = new ArrayList<>();

		request.add(String.format("Action: %s", getName()));

		if (getActionId() != null)
			request.add(String.format("ActionID: %s", getActionId()));

		return request;
	}

	@Override
	protected boolean engage(String attr, String value) {
		super.engage(attr, value);

		log.warn(String.format("Useless use of method 'engage': attribute \"%s\" for message of type \"%s\". Value: \"%s\"", attr, getType(), value));
		return true;
	}

	public void submit() {
		getAMI().submit(this);
	}
}
