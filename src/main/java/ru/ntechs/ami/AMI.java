package ru.ntechs.ami;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import ru.ntechs.ami.actions.Action;
import ru.ntechs.ami.actions.Login;
import ru.ntechs.ami.events.FullyBooted;
import ru.ntechs.ami.events.PeerStatus;
import ru.ntechs.ami.responses.Success;

@Component
@EnableConfigurationProperties(Config.class)
public class AMI extends Thread {
	private Config config;

	private Integer verMajor;
	private Integer verMinor;

	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;

	private Message message;
	private final Logger logger = LoggerFactory.getLogger(AMI.class);

	private ConcurrentHashMap<String, Vector<EventHandler>> handlersMap = new ConcurrentHashMap<>();
	private ExecutorService handlerThreadPool = Executors.newFixedThreadPool(1);

	public AMI(Config config) {
		super();

		this.config = config;

		setDaemon(true);
		setName("ami");
		reset();
		start();
	}

	@Override
	public void run() {
		while (true) {
			try {
				String ln;

				logger.info(String.format("connecting to ami://%s:%d...", config.getHostname(), config.getPort()));

				connect(config.getHostname(), config.getPort());

				submit(new Login(this, config.getUsername(), config.getPassword()));
				ln = in.readLine();

				final String amiName = "Asterisk Call Manager/";

				if (ln.startsWith("Asterisk Call Manager/")) {
					String version = ln.substring(amiName.length());
					Integer pointPos = version.indexOf('.');

					try {
						verMajor = Integer.decode(version.substring(0, pointPos));
						verMinor = Integer.decode(version.substring(pointPos + 1));

						logger.info(String.format("successfully connected to ami://%s:%d, protocol version: %d.%d", config.getHostname(), config.getPort(), verMajor, verMinor));
					} catch (NumberFormatException e) {
						logger.info(String.format("successfully connected to ami://%s:%d", config.getHostname(), config.getPort(), verMajor, verMinor));
						logger.error(String.format("Failed to parse AMI version: %s", version));
					}
				}

				while ((ln = in.readLine()) != null) {
					if (!ln.isEmpty()) {
						Integer pos = ln.indexOf(':');
						String attr = ln.substring(0, pos).trim();
						String value = ln.substring(pos + 1).trim();

						if (message == null) {
							if (attr.equalsIgnoreCase("Event")) {
								if (value.equalsIgnoreCase("PeerStatus"))
									message = new PeerStatus(this, value);
								else if (value.equalsIgnoreCase("FullyBooted"))
									message = new FullyBooted(this, value);
							}
							else if (attr.equalsIgnoreCase("Response")) {
								if (value.equalsIgnoreCase("Success"))
									message = new Success(this, value);
							}

							if (message == null)
								message = new PlainMessage(this, attr, value);
						}
						else
							message.engage(attr, value);
					}
					else {
						if ((message instanceof PlainMessage) && (message.getType().equals("Response")))
							message.dump();

						if (message.getName().length() > 0) {
							Vector<EventHandler> queue = handlersMap.get(message.getName().toLowerCase());

							if (queue != null) {
								for (EventHandler handler : queue) {
									handlerThreadPool.execute(new Runnable() {
										private Message messageLocal = message;

										@Override
										public void run() {
											try {
												handler.run(messageLocal);
											}
											catch (Exception e) {
												e.printStackTrace();
											}
										}
									});
								}
							}
						}

						message = null;
					}
				}
			} catch (UnknownHostException e) {
				logger.error(String.format("Unable to connect to Asterisk Manager Interface (AMI): %s", e.getLocalizedMessage()));
			} catch (IOException e) {
				logger.error(String.format("I/O with Asterisk Manager Interface (AMI) failed: %s", e.getLocalizedMessage()));
			} finally {
				reset();

				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {}
			}
		}
	}

	public void addHandler(String eventName, EventHandler handler) {
		eventName = eventName.toLowerCase();
		Vector<EventHandler> queue = handlersMap.get(eventName);

		if (queue == null) {
			queue = new Vector<>();
			handlersMap.put(eventName, queue);
		}

		queue.add(handler);
	}

	public void submit(Action cmd) {
		for (String str : cmd.getMessageText()) {
			logger.info(String.format("sending: %s", str));
			out.println(str);
		}

		logger.info("sending: <LF>");
		out.println();
	}

	private void connect(String hostname, Integer port) throws UnknownHostException, IOException {
		if (config.getHostname() == null) {
			logger.error("ami.hostname not defined in application.properties");
			return;
		}

		if (config.getPort() == null) {
			logger.error("ami.port not defined in application.properties");
			return;
		}

		if (config.getUsername() == null) {
			logger.error("ami.username not defined in application.properties");
			return;
		}

		if (config.getPassword() == null) {
			logger.error("ami.password not defined in application.properties");
			return;
		}

		socket = new Socket(hostname, port);

		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	private void reset() {
		this.socket = null;
		this.in = null;
		this.out = null;
		this.message = null;
	}
}
