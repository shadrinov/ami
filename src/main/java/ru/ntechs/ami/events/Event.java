package ru.ntechs.ami.events;

import ru.ntechs.ami.AMI;
import ru.ntechs.ami.Message;

public abstract class Event extends Message {

	public Event(AMI ami, String name) {
		super(ami, "Event", name);
	}
}
