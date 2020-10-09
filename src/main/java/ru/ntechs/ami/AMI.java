package ru.ntechs.ami;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import lombok.extern.slf4j.Slf4j;
import ru.ntechs.ami.actions.Action;
import ru.ntechs.ami.actions.Login;
import ru.ntechs.ami.events.FullyBooted;
import ru.ntechs.ami.events.PeerStatus;
import ru.ntechs.ami.responses.Error;
import ru.ntechs.ami.responses.Response;
import ru.ntechs.ami.responses.Success;

@Slf4j
public class AMI extends Thread {
	private final static String AMI_HEADER = "Asterisk Call Manager/";

	private String hostname;
	private int port;
	private String username;
	private String password;

	private Integer verMajor;
	private Integer verMinor;
	private Integer verSuperminor;

	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;

	private boolean debug = false;

	private ConcurrentLinkedDeque<EventHandler> universalHandlers = new ConcurrentLinkedDeque<>();
	private ConcurrentHashMap<String, ConcurrentLinkedDeque<EventHandler>> handlersMap = new ConcurrentHashMap<>();

	public AMI() {
		super();

		setDaemon(true);
		setName("ami");
		reset();
	}

	@Override
	public void run() {
		while (true) {
			try {
				log.info("connecting to ami://{}:{}...", hostname, port);
				connect(hostname, port);

				final Login login = new Login(this, username, password);

				new Thread(new Runnable() {
					@Override
					public void run() {
						login.submit();
						Response resp = login.waitForResponse(30000);

						if (resp.isSuccess())
							log.info("login successful: {}", resp.getMessage());
						else
							log.info("login failed: {}", resp.getMessage());
					}
				}).start();

				String ln = in.readLine();
				if (ln.startsWith(AMI_HEADER)) {
					String version = ln.substring(AMI_HEADER.length());

					try {
						int pointPos, pointPos2;

						pointPos = version.indexOf('.');
						verMajor = Integer.decode(version.substring(0, pointPos++));

						if ((pointPos2 = version.indexOf('.', pointPos)) != -1) {
							verMinor = Integer.decode(version.substring(pointPos, pointPos2++));
							verSuperminor = Integer.decode(version.substring(pointPos2));
						}
						else {
							verMinor = Integer.decode(version.substring(pointPos));
							verSuperminor = 0;
						}

						log.info(String.format("connected to ami://%s:%d, protocol version: %d.%d.%d (%s)", hostname, port, verMajor, verMinor, verSuperminor, version));
					} catch (NumberFormatException e) {
						log.info(String.format("connected to ami://%s:%d", hostname, port));
						log.error(String.format("Failed to parse AMI version: %s", version));
					}
				}

				Message message = null;
				while ((ln = in.readLine()) != null) {
					if (!ln.isEmpty()) {
						Integer pos = ln.indexOf(':');
						String attr = ln.substring(0, pos).trim();
						String value = ln.substring(pos + 1).trim();

						if (message == null) {
							switch (attr.toLowerCase()) {
							case ("event"):
								switch (value.toLowerCase()) {
									case ("peerstatus"): message = new PeerStatus(this, value); break;
									case ("fullybooted"): message = new FullyBooted(this, value); break;
								}
							case ("response"):
								switch (value.toLowerCase()) {
									case ("success"): message = new Success(this, value); break;
									case ("error"): message = new Error(this, value); break;
								}
							}

							if (message == null)
								message = new PlainMessage(this, attr, value);
						}
						else
							message.engage(attr, value);
					}
					else {
						if (debug)
							message.dump("received: ");

						if (!message.getName().isEmpty()) {
							Message messageLocal = message;
							universalHandlers.forEach(handler -> { handler.run(messageLocal); });

							ConcurrentLinkedDeque<EventHandler> queue = handlersMap.get(message.getName().toLowerCase());

							if (queue != null)
								queue.forEach(handler -> { handler.run(messageLocal); });
						}

						message = null;
					}
				}
			} catch (UnknownHostException e) {
				log.error(String.format("Unable to connect to Asterisk Manager Interface (AMI): %s", e.getLocalizedMessage()));
			} catch (IOException e) {
				log.error(String.format("I/O with Asterisk Manager Interface (AMI) failed: %s", e.getLocalizedMessage()));
			} finally {
				reset();

				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {}
			}
		}
	}

	public EventHandlerDescriptor addHandler(String eventName, EventHandler handler) {
		eventName = eventName.toLowerCase();

		ConcurrentLinkedDeque<EventHandler> queue = handlersMap.get(eventName);

		if (queue == null) {
			queue = new ConcurrentLinkedDeque<>();
			handlersMap.put(eventName, queue);
		}

		queue.add(handler);
		return new EventHandlerDescriptor(queue, handler);
	}

	public EventHandlerDescriptor addHandler(EventHandler handler) {
		universalHandlers.add(handler);
		return new EventHandlerDescriptor(universalHandlers, handler);
	}

	public synchronized void submit(Action cmd) {
		if (debug)
			cmd.dump("sending: ");

		for (String str : cmd.getMessageText())
			out.println(str);

		out.println();
	}

	private void connect(String hostname, Integer port) throws UnknownHostException, IOException {
		socket = new Socket(hostname, port);

		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	private void reset() {
		this.socket = null;
		this.in = null;
		this.out = null;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void enableDebug() {
		debug = true;
	}

	public void disableDebug() {
		debug = false;
	}

	public int compareVersion(int verMajor, int verMinor, int verSuperminor) {
		if (this.verMajor != null) {
			if (this.verMajor == verMajor) {
				if (this.verMinor != null) {
					if (this.verMinor == verMinor) {
						if (this.verSuperminor != null) {
							if (this.verSuperminor == verSuperminor)
								return 0;
							else
								return (this.verSuperminor > verSuperminor) ? 1 : -1;
						}
						else
							log.info("unable to check AMI version, verSuperminor not defined");
					}
					else
						return (this.verMinor > verMinor) ? 1 : -1;
				}
				else
					log.info("unable to check AMI version, verMinor not defined");
			}
			else
				return (this.verMajor > verMajor) ? 1 : -1;
		}
		else
			log.info("unable to check AMI version, verMajor not defined");

		return -1;
	}
}
