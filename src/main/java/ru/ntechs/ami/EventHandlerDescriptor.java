package ru.ntechs.ami;

import java.util.Vector;

public class EventHandlerDescriptor {
	private Vector<EventHandler> queue;
	private EventHandler obj;

	public EventHandlerDescriptor(Vector<EventHandler> queue, EventHandler obj) {
		super();

		this.queue = queue;
		this.obj = obj;
	}

	public synchronized void cancel() {
		queue.remove(obj);

		queue = null;
		obj = null;
	}
}
