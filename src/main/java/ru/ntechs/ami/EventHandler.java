package ru.ntechs.ami;

@FunctionalInterface
public interface EventHandler {
	public void run(Message message);
}
