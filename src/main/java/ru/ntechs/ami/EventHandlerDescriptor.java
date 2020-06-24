package ru.ntechs.ami;

import java.util.concurrent.ConcurrentLinkedDeque;

public class EventHandlerDescriptor {
	private ConcurrentLinkedDeque<EventHandler> queue;
	private EventHandler obj;

	public EventHandlerDescriptor(ConcurrentLinkedDeque<EventHandler> queue, EventHandler obj) {
		super();

		this.queue = queue;
		this.obj = obj;
	}

	public synchronized void cancel() {
		if ((queue != null) && (obj != null)) {
			queue.remove(obj);

			queue = null;
			obj = null;
		}
	}
}
