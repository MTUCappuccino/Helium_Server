
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerMessaging implements Runnable {

	// Tracks those online
	ArrayList<Person> online = new ArrayList<Person>();

	// Tracks those who "died" LOL
	ArrayList<Ghost> offline = new ArrayList<Ghost>();

	// Tracks up to the last 300 hundred messages
	protected CircleQue messQueue = new CircleQue(300);

	// Delete
	private boolean delete = false;
	private boolean edit = false;

	// Tracks stats.
	public int messagesSent = 0;
	public int messEdited = 0;
	public int born = 0;
	public int died = 0;
	public int kicked = 0;

	// Tracks if this thread is running/ control variable
	private boolean open = true;

	// Controlls for kicking
	protected boolean kicking = false;
	private String target;

	// Tracking this thread
	protected Thread t;
	Trap trap;
	boolean enable = false;

	public ServerMessaging() {
		t = new Thread(this);
		t.setName("ServerMessaging");
		t.start();
	}

	@Override
	public void run() {
		while (open) {

			// Checks for kick
			if (kicking) {
				try {
					kick(target);
					kicking = false;
					System.out.println("HIT " + target);
				} catch (IOException e) {
					System.out.println("Failed kick");
					kicking = false;
				}
			}

			check(enable);
			enable = false;

			for (int i = 0; i < online.size(); i++) {
				try {
					if (online.get(i).getInput().ready()) {

						String mess = online.get(i).getInput().readLine();
						Message m = MessIN(mess);

						// add to queue

						System.out.println("Got message from " + online.get(i).getHandle());

						intakeDec(m, online.get(i));

					} else {
						// check that person still good
						/*
						 * if (!heartbeat(online.get(i))) { online.remove(i);
						 * died += 1; }
						 */

					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException ex) {
				Logger.getLogger(ServerMessaging.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void close() {

		Message disconnect = new Message(Message.MessageType.CLOSE_CONNECTION, Message.ContentType.TEXT, "Server",
				("I was shut down...\n"));

		// try {
		push(disconnect);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		open = false;
	}

	/**
	 * push pushes message to all list persons currently joined
	 * 
	 * @param message
	 * @throws IOException
	 */
	protected void push(Message message) {

		ArrayList<Person> failures = new ArrayList<Person>();

		for (int i = 0; i < online.size(); i++) {
			try {
				online.get(i).getOutput().write(message.toString());
				online.get(i).getOutput().flush();
			} catch (IOException e) {
				failures.add(online.get(i));
			}
		}

		for (int i = 0; i < failures.size(); i++) {
			online.remove(failures.get(i));
			System.out.println("HIT");
			died += 1;
		}

	}

	/**
	 * tinyPush pushes to a single person
	 * 
	 * @param message
	 * @param person
	 * @throws IOException
	 */
	protected void tinyPush(Message message, Person person) {
		if (message != null)
			try {
				person.getOutput().write(message.toString());
				person.getOutput().flush();
			} catch (IOException e) {
				online.remove(person);
			}

	}

	/**
	 * closeMess closes messaging thread
	 * 
	 * @return
	 */
	public boolean closeMess() {
		open = false;
		return true;
	}

	/**
	 * MessIN takes in message from client then splits into parts, creates new
	 * message on server end
	 * 
	 * @param mess
	 *            String in client message format
	 * @return Message
	 */
	private Message MessIN(String mess) {
		String[] unparsedSegments = mess.split(",");

		Message.MessageType type = Message.MessageType.values()[Integer.parseInt(unparsedSegments[0])];
		int id = Integer.parseInt(unparsedSegments[1]);
		Message.ContentType contentType = Message.ContentType.values()[Integer.parseInt(unparsedSegments[2])];
		String sender = unparsedSegments[3];

		String userMessage;

		try {
			userMessage = unparsedSegments[4];// spliter
		} catch (ArrayIndexOutOfBoundsException e) {
			userMessage = "";
		}

		Message m = new Message(type, id, contentType, sender, System.currentTimeMillis(), userMessage);

		return m;
	}

	protected void messageDecsSERVER(Message m) throws IOException {
		switch (m.getType()) {

		case NEW_MESSAGE:
			m.setId(messQueue.count);
			messQueue.add(m);
			messagesSent += 1;
			push(m);
			break;
		case EDIT_MESSAGE:
			break;
		case DELETE_MESSAGE:
			break;
		case CLOSE_CONNECTION:

			break;
		case LEAVE_SERVER:

			break;
		case UPDATE_SERVER_DATA:
			break;
		default:
			break;
		}
	}

	/**
	 * ghostCheck sees if someone was this handle, and gives them missed
	 * messages
	 * 
	 * @param person
	 */
	protected void ghostCheck(Person person) {

		for (int i = 0, g = offline.size(); i < g; i++) {
			if (offline.get(i).getHandle().equals(person.getHandle())) {

				String match = messQueue.messDump(offline.get(i).getLastKnown());
				offline.remove(i);

				String[] split = match.split(",");
				int min = Integer.parseInt(split[0]);
				int max = Integer.parseInt(split[1]);

				for (; min < max; min++) {

					if (online.size() > 0)
						tinyPush(messQueue.get(min), person);
				}
				return;
			}

		}
	}

	/**
	 * heartbeat constantly checks everyone is online
	 * 
	 * @param p
	 *            Perosn
	 * @return true if alive
	 * @throws IOException
	 */
	private boolean heartbeat(Person p) throws IOException {

		Message beat = new Message(Message.MessageType.HEARTBEAT, Message.ContentType.TEXT, "Server", null);

		tinyPush(beat, p);
		return true;
	}

	/**
	 * kick kicks all people with specified handle
	 * 
	 * @param handle
	 *            String
	 * @return true if people are kicked
	 * @throws IOException
	 *             Fails to send messages
	 */
	protected boolean kick(String handle) throws IOException {
		// ArrayList tracking all who will be kicked
		ArrayList<Person> match = new ArrayList<Person>();
		// Says if people where kicked
		boolean booted = false;
		// Tracking variable
		int siz = online.size();

		// First Loop: looks for people with matching handles
		for (int i = 0; i < siz; i++) {
			if (online.get(i).getHandle().equals(handle)) {
				match.add(online.get(i));
				booted = true;
			}
		}

		// Second Loop: kicks all matches
		for (int i = match.size() - 1; i >= 0; i--) {

			Message kick = new Message(Message.MessageType.CLOSE_CONNECTION, Message.ContentType.TEXT, "Server",
					("You were kicked\n"));
			tinyPush(kick, match.get(i));
			online.remove(match.get(i));

			kick = new Message(Message.MessageType.NEW_MESSAGE, Message.ContentType.TEXT, "Server",
					(handle + " was kicked\n"));
			messageDecsSERVER(kick);

			kicked += 1;
		}

		// returns if any was booted or not
		return booted;
	}

	/**
	 * setTarget sets target for kicking
	 * 
	 * @param handle
	 */
	public void setTarget(String handle) {
		target = handle;
	}

	/**
	 * deleteChange toggles the delete message setting
	 * 
	 * @return
	 */
	public String deleteChange() {
		if (delete == true) {
			delete = false;
			return "Delete: disabled";
		} else {
			delete = true;
			return "Delete: enabled";
		}
	}

	/**
	 * editChange toggles the edit message setting
	 * 
	 * @return
	 */
	public String editChange() {
		if (edit == true) {
			edit = false;
			return "Edit: disabled";
		} else {
			edit = true;
			return "Edit: enabled";
		}
	}

	/**
	 * intakeDec takes in user messages and decides what to do with them
	 * 
	 * @param m
	 * @param p
	 */
	private void intakeDec(Message m, Person p) {
		switch (m.getType()) {

		case NEW_MESSAGE:
			m.setId(messQueue.count);
			messQueue.add(m);
			messagesSent += 1;
			push(m);
			break;
		case EDIT_MESSAGE:
			if (edit) {
				messEdited += 1;
				push(m);
				m.setType(Message.MessageType.NEW_MESSAGE);
				messQueue.set(m.getId(), m);
			}
			break;
		case DELETE_MESSAGE:
			if (delete) {
				push(m);
				messQueue.set(m.getId(), null);
			}
			break;
		case CLOSE_CONNECTION:
			Message left = new Message(Message.MessageType.NEW_MESSAGE, Message.ContentType.TEXT, "Server",
					("User" + p.getHandle() + "left\n"));

			Ghost ghost = new Ghost(p, messQueue.count);

			left.setId(messQueue.count);
			messQueue.add(left);

			offline.add(ghost);
			online.remove(p);

			push(left);
			break;
		case LEAVE_SERVER:
			Message leave = new Message(Message.MessageType.NEW_MESSAGE, Message.ContentType.TEXT, "Server",
					("User" + p.getHandle() + "dissconnected\n"));

			leave.setId(messQueue.count);
			messQueue.add(leave);

			online.remove(p);
			died += 1;

			push(leave);
			break;
		case UPDATE_SERVER_DATA:
			break;
		default:
			break;

		}
	}

	public void trap() {
		trap = new Trap();
		enable = true;
	}

	private void check(boolean enable) {
		if (enable) {
			ArrayList<Person> alive = new ArrayList<Person>();
			for (int i = 0; i < online.size(); i++) {
				alive.add(online.get(i));
			}
			ArrayList<Person> wait = new ArrayList<Person>();

			while (alive.size() > 1) {
				trap.question();

				Message q = new Message(Message.MessageType.NEW_MESSAGE, Message.ContentType.TEXT, "Mad AI",
						trap.currentQuestion);

				push(q);

				for (int i = 0; i < alive.size(); i++) {
					wait.add(alive.get(i));
				}

				while (wait.size() > 0) {
					for (int i = 0; i < wait.size(); i++) {
						try {

							if (wait.get(i).getInput().ready()) {

								String mess = wait.get(i).getInput().readLine();
								Message m = MessIN(mess);

								if (!m.getContent().equals(trap.currentAnswer) || m.getContent() == null) {
									Message dead = new Message(Message.MessageType.CLOSE_CONNECTION,
											Message.ContentType.TEXT, "Mad AI", "WRONG: you have died");
									tinyPush(dead, wait.get(i));
									alive.remove(wait.get(i));
									online.remove(wait.get(i));
									wait.remove((i));
									i--;

								} else {
									Message right = new Message(Message.MessageType.NEW_MESSAGE,
											Message.ContentType.TEXT, "Mad AI", "RIGHT: you will live, for now...");
									tinyPush(right, wait.get(i));
									wait.remove(i);
									i--;
								}

							} else {
							}

						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			Message win = new Message(Message.MessageType.NEW_MESSAGE, Message.ContentType.TEXT, "Mad AI", "You win!");
			for (int i = 0; i < alive.size(); i++) {
				tinyPush(win, alive.get(i));
			}
			enable = false;
		}
	}
}
