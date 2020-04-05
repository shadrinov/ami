package ru.ntechs.ami.actions;

import java.util.ArrayList;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import ru.ntechs.ami.AMI;
import ru.ntechs.ami.EventHandler;
import ru.ntechs.ami.EventHandlerDescriptor;
import ru.ntechs.ami.Message;
import ru.ntechs.ami.responses.Response;

@Slf4j
public abstract class Action extends Message {
	private String actionId;

	private EventHandlerDescriptor dscSuccess;
	private EventHandlerDescriptor dscError;

	private Response response;
	private Object lock;

	public Action(AMI ami, String name) {
		super(ami, "Action", name);

		actionId = UUID.randomUUID().toString();
		lock = new Object();

		EventHandler eventHandler = new EventHandler( ) {
			@Override
			public void run(Message message) {
				synchronized (lock) {
					if (message instanceof Response) {
						Response responseLocal = (Response) message;

						if (responseLocal.getActionId().equals(actionId)) {
							response = responseLocal;

							dscSuccess.cancel();
							dscError.cancel();

							lock.notifyAll();
						}
					}
				}
			}
		};

		dscSuccess = getAMI().addHandler("Success", eventHandler);
		dscError = getAMI().addHandler("Error", eventHandler);
	}

	@Override
	public void dump(String prefix) {
		for (String str : getMessageText())
			log.info("{}{}", prefix, str);

		log.info("{}--- End Of Message ---", prefix);
	}

	public ArrayList<String> getMessageText() {
		ArrayList<String> request = new ArrayList<>();

		request.add(String.format("Action: %s", getName()));

		if (actionId != null)
			request.add(String.format("ActionID: %s", actionId));

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

	public Response waitForResponse(long timeout) {
		synchronized (lock) {
			if (response == null) {
				try {
					lock.wait(timeout);
				} catch (InterruptedException e) {}
			}
		}

		return response;
	}
}
