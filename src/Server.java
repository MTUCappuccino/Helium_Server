
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Server the Sever for Helium
 *
 * @author Lucas_Catron
 *
 */
public class Server implements Runnable {

	// Tracks if server open
	private boolean open = true;

	// Tracks if handles required
	protected boolean handles;

	// Tracks if password required
	private boolean passes;

	// Tracks server type
	private String serverType;

	// Tracks port number
	private int port;

	// Tracks server socket
	private ServerSocket listener;

	// The server name
	private String serverName = "";

	// The password
	private String password = "";

	// Default server color is black
	private String hexColor = "000000";

	// The Icon URL
	private String IconURL = "NULL";

	// Tracks if server is public or not
	public boolean public_;

	// TEMP Central info 
	String hostName = "141.219.201.139";
	int hostPort = 9090;
	public String code = "";
	
	// Tracking this thread
	protected Thread t;

	// Opening messaging thread
	ServerMessaging messaging = new ServerMessaging();

	/**
	 * Server
	 *
	 * Constructor; sets the port number, and name, password, and whether server
	 * will require handles and passwords
	 *
	 * @param portNum
	 *            number to set port to
	 * @param name
	 *            name of server
	 * @param pass
	 *            password to set
	 * @param passesI
	 *            If server requires password
	 * @param handlesI
	 *            If server requires handles
	 */
	Server(int portNum, String name, String pass, boolean passesI, boolean handlesI, boolean poblic) {
		setPort(portNum);
		setName(name);
		setPassword(pass);
		handles = handlesI;
		passes = passesI;
		public_ = poblic;
	}

	/**
	 * openServer opens new server, starts new thread
	 *
	 * @return
	 */
	public boolean openServer() {
		t = new Thread(this);
		t.start();
		t.setName("AcceptThread");
		return true;
	}

	/**
	 * closeServer() Closes the server, and the messaging thread
	 * 
	 * @return boolean, successful close = true
	 */
	public boolean closeServer() {
		open = false;
		try {
			listener.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		messaging.close();
		return true;
	}

	@Override
	public void run() {

		serverType = createServerType();

		try {
			// Open server port
			listener = new ServerSocket(port);
			open = true;
			try {
				while (open) {
					Socket socket = listener.accept();

					System.out.println();

					try {
						// make a new person to track user
						Person person = new Person(socket);

						// Server to client for data request
						initalOut(person);

						// client to server with required info
						initalIn(person);

						// Check to see if they sent the right info
						if (person.getStatus()) {
							welcome(person);

						} else {

							outSeverMessage(person, "invalid_password\n");
							initalIn(person);

							if (person.getStatus()) {
								welcome(person);
							}

						}
					} finally {

					}
					System.out.print("");
				}

			} finally {
				listener.close();
			}
		} catch (IOException e) {

		}

	}

	/**
	 * initialOut first contact with client begins, sends Initial data asking
	 * requirements
	 * 
	 * @param socket
	 * @throws IOException
	 */
	private void initalOut(Person person) throws IOException {
		person.getOutput().write(serverType + "\n");
		person.getOutput().flush();
		// out.close();
	}

	/**
	 * initalIn This method MUST run until input receive or time out. Takes
	 * response from client.
	 *
	 * @param socket
	 * @return
	 * @throws IOException
	 */
	private void initalIn(Person p) throws IOException {

		
		String answer = p.getInput().readLine();
		String[] split = answer.split(",");

		byte nameLength = Byte.parseByte(split[0]);
		byte passLength = Byte.parseByte(split[1]);
		int junkread = split[0].length() + split[1].length() + 2;

		p.setHandle(answer.substring(junkread, junkread + nameLength));

		String pass = answer.substring(junkread + nameLength , junkread + nameLength + passLength );

		if (serverType.equals("01") || serverType.equals("11")) {
			p.setStatus(checkPassword(pass));
			if (p.getStatus() == false) {
//				System.out.println("Failed password attempt");
			}
		} else {
			p.setStatus(true);
		}
	}

	/**
	 * outSetup sends data based on first response to build on client side.
	 * 
	 * @param person
	 * @throws IOException
	 */
	private void outSetup(Person person) throws IOException {
		person.getOutput().write(serverName + ";" + hexColor + ";" + IconURL + "\n");
		person.getOutput().flush();
	}

	/**
	 * welcome Sends out data, adds person to online, welcome message, server
	 * mess, ghost check.
	 * 
	 * @param person
	 * @throws IOException
	 */
	private void welcome(Person person) throws IOException {
		// If true, send them server data
		outSetup(person);

		// Add them to the currently online users
		messaging.online.add(person);

		// Make welcome message
		Message welcome = new Message(Message.MessageType.NEW_MESSAGE, Message.ContentType.TEXT, "Server",
				(handles ? person.getHandle() +" joined\n" : "User joined\n"));

		// Send welcome message
		messaging.messageDecsSERVER(welcome);

		// Tell server admin someone joined
		System.out.print((handles ? person.getHandle() + " joined\n" : "User joined\n"));
		messaging.born += 1;

		// Check if the handle was that of a ghost
		messaging.ghostCheck(person);
	}

	/**
	 * outServerMessage sends direct unformatted messages from server to a user.
	 * Mainly for testing
	 * 
	 * @param person
	 * @param mess
	 *            String message
	 * @throws IOException
	 */
	private void outSeverMessage(Person person, String mess) throws IOException {
		person.getOutput().write(mess);
		person.getOutput().flush();
	}

	/**
	 * THIS IS A UNTRACKED PUSH only use for client setting pushes
	 * 
	 * @param mess
	 */
	private void outServerAll(Message mess) {
//		try {
			messaging.push(mess);
//		} catch (IOException e) {
//			System.out.println("Failed Server push\n");
//			e.printStackTrace();
//		}
	}

	/**
	 * createServerType creates server type based on handles and passes
	 * 
	 * @return String 00, 10, 01, 11
	 */
	private String createServerType() {
		return (handles ? "1" : "0") + (passes ? "1" : "0");
	}

	/**
	 * update Sends update to all clients of new server data
	 */
	public void update() {
		Message update = new Message(Message.MessageType.UPDATE_SERVER_DATA, Message.ContentType.TEXT, "Server",
				serverName + ";" + hexColor + ";" + IconURL + "\n");
		outServerAll(update);
	}

	/**
	 * 
	 * @param ip
	 * @return
	 */
	public String reg(String ip) {
		String reply;

		// Getting local port server set on
		int prt = listener.getLocalPort();
		String plt = String.valueOf(prt);

		// Creating message for central
		String mess = ("" + serverName.length() + "," + IconURL.length() + "," + ip.length() + "," + plt.length() + ","
				+ (handles ? "1" : "0") + (passes ? "1" : "0") + (public_ ? "1" : "0") + "," + serverName
				+ IconURL + ip + prt);
		// return mess;

		try {
			Socket s = new Socket(hostName, hostPort);

			BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
			BufferedWriter output = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

			output.write(mess + "\n");
			output.flush();
			
			reply = input.readLine();
			s.close();
		} catch (IOException e) {
//			e.printStackTrace();
			return mess;
			
		}

		code = reply;
		return reply;
	}

	// \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\ALL BELOW ARE SET, GET, CHECK, METHODS/////////////////////////////////
	/**
	 * setPort
	 *
	 * @param num
	 */
	public void setPort(int num) {
		port = num;
	}

	/**
	 * getPort
	 *
	 * @return the port number
	 */
	public int getPort() {
		return port;
	}

	/**
	 * setName sets server name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		serverName = name;
	}

	/**
	 * getName returns server name
	 * 
	 * @return serverName
	 */
	public String getName() {
		return serverName;
	}

	/**
	 * sets the password
	 * 
	 * @param x:
	 *            the password
	 * @return : if set
	 */
	protected boolean setPassword(String x) {
		password = x;
		return true;
	}

	/**
	 * checkPassword sees if input matches set password
	 * 
	 * @param x
	 * @return: if true
	 */
	private boolean checkPassword(String x) {
		return x.equals(password);
	}

	/**
	 * setColor sets hex color for server
	 * 
	 * @param x
	 */
	public void setHexColor(String x) {
		hexColor = x;
	}

	/**
	 * setIconURL sets ICON URL
	 * 
	 * @param x
	 */
	public void setIconURL(String x) {
		IconURL = x;
	}

	/**
	 * getMessagesSent stat method, returns user to user number of messages
	 * 
	 * @return int number of messages
	 */
	public int getMessagesSent() {
		return messaging.messagesSent;
	}

	/**
	 * getMessEdited
	 *  number of edited messages
	 * @return int number of messages
	 */
	public int getMessEdited() {
		return messaging.messEdited;
	}
	
	/**
	 * kick
	 * hands down info for kick attempt
	 * @param handle target for messaging
	 */
	public void kick(String handle) {
		messaging.setTarget(handle);
		messaging.kicking = true;
	}
	
	/**
	 * deleteChange toggles weather on not users can delete messages
	 * @return
	 */
	public String deleteChange() {
		return messaging.deleteChange();
	}
	
	/**
	 * editChange toggles weather or not users can edit their messages
	 * @return
	 */
	public String editChange() {
		return messaging.editChange();
	}
	
	/**
	 * getPass
	 * returns weather or not this server requires a password
	 * @return
	 */
	public boolean getPass() {
		return passes;
	}
	
	public void trap() {
		messaging.trap();
	}

}
