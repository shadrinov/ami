package ru.ntechs.ami;

public abstract class Response extends Message {

	public Response(AMI ami, String name) {
		super(ami, "Response", name);
	}
}
