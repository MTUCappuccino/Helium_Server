
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

	// Tracks stats.
	public int messagesSent = 0;
	public int messEdited = 0;
	public int born = 0;
	public int died = 0;

	// Tracks if this thread is running/ control variable
	private boolean open = true;

	// Tracking this thread
	protected Thread t;

	public ServerMessaging() {
		t = new Thread(this);
		t.setName("ServerMessaging");
		t.start();
	}

	@Override
	public void run() {
		while (open) {
			for (int i = 0; i < online.size(); i++) {
				try {
					if (online.get(i).getInput().ready()) {

						String mess = online.get(i).getInput().readLine();
						Message m = MessIN(mess);

						// add to queue

						System.out.println("Got message from " + online.get(i).getHandle());

						switch (m.getType()) {

						case NEW_MESSAGE:
							m.setId(messQueue.count);
							messQueue.add(m);
							messagesSent += 1;
							push(m);
							break;
						case EDIT_MESSAGE:
							messEdited += 1;
							push(m);
							m.setType(Message.MessageType.NEW_MESSAGE);
							messQueue.set(m.getId(), m);
							break;
						case DELETE_MESSAGE:
							// Need implement
							break;
						case CLOSE_CONNECTION:
							Message left = new Message(Message.MessageType.NEW_MESSAGE, Message.ContentType.TEXT,
									"Server", ("User" + online.get(i).getHandle() + "left\n"));

							Ghost ghost = new Ghost(online.get(i), messQueue.count);

							left.setId(messQueue.count);
							messQueue.add(left);

							offline.add(ghost);
							online.remove(i);

							push(left);
							break;
						case LEAVE_SERVER:
							Message leave = new Message(Message.MessageType.NEW_MESSAGE, Message.ContentType.TEXT,
									"Server", ("User" + online.get(i).getHandle() + "dissconnected\n"));

							leave.setId(messQueue.count);
							messQueue.add(leave);

							online.remove(i);
							died += 1;

							push(leave);
							break;
						case UPDATE_SERVER_DATA:
							break;
						default:
							break;

						}

					} else {
						// check that person still good
/*						if (!heartbeat(online.get(i))) {
							online.remove(i);
							died += 1;
						}*/

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

//		try {
			push(disconnect);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

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
		
		for(int i = 0; i < failures.size(); i ++) {
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
	protected void tinyPush(Message message, Person person) throws IOException {

		person.getOutput().write(message.toString());
		person.getOutput().flush();
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
		String userMessage = unparsedSegments[4];// spliter

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
					try {
						if (online.size() > 0)
							tinyPush(messQueue.get(min), person);
					} catch (IOException e) {
						System.out.println("Ghost Update fail");
					}
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

		try {

			Message beat = new Message(Message.MessageType.HEARTBEAT, Message.ContentType.TEXT, "Server", null);

			tinyPush(beat, p);
			return true;

		} catch (IOException e) {

			return false;
		}
	}

	/**
	 * kick
	 * kicks all people with specified handle
	 * @param handle String
	 * @return true if people are kicked
	 * @throws IOException Fails to send messages
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
		for(int i = match.size(); i < 0; i--) {
			
		Message kick = new Message(Message.MessageType.NEW_MESSAGE, Message.ContentType.TEXT, "Server",
				(handle + "was kicked\n"));
		messageDecsSERVER(kick);
		
		kick = new Message(Message.MessageType.CLOSE_CONNECTION, Message.ContentType.TEXT, "Server",
				(handle + "was kicked\n"));
		tinyPush(kick, match.get(i));
		}
		
		// returns if any was booted or not
		return booted;
	}
}
