
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

/**
 * Server the Sever for Helium
 *
 * @author Lucas_Catron
 *
 */
public class Server implements Runnable {

	// Tracks if server open
	private boolean open = true;
	private boolean handles;
	private boolean passes;
	private String serverType;

	private int port;
	private ServerSocket listener;
	private String serverName = "";
	private String password = "";
	//Default server color is black
	private String hexColor = "000000";
	private String customBack = "NULL";

	public boolean public_;
	Thread t; //= new Thread(this);
	ServerMessaging online = new ServerMessaging();

	/**
	 * Server
	 *
	 * Constructor; sets the port number, and name, password, and whether server will require 
	 * 		handles and passwords
	 *
	 * @param portNum
	 * 		number to set port to
	 * @param name
	 * 		name of server
	 * @param pass
	 * 		password to set
	 * @param passesI
	 * 		If server requires password
	 * @param handlesI
	 * 		If server requires handles
	 */
	Server(int portNum, String name, String pass, boolean passesI, boolean handlesI) {
		setPort(portNum);
		setName(name);
		setPassword(pass);
		handles = handlesI;
		passes = passesI;	
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
	 * closeServer()
	 *		Closes the server, and the messaging thread
	 * @return boolean, successful close = true
	 */
	public boolean closeServer() {
		open = false;
		try {
			listener.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		online.close();
		return true;
	}

	@Override
	public void run() {
		
		// create the listener while/try
		
		// listen on the port while
		
		// clean up stuff
		
		serverType = createServerType();
		
		try {
			listener = new ServerSocket(port);
			open = true;
			try {
				while (open) {
					System.out.println("Waiting for a client...");
					Socket socket = listener.accept();
					System.out.println("Accepted a socket...");
					System.out.println();
					
					try {
						Person person = new Person(socket);
						initalOut(person);
						initalIn(person);

						if (person.getStatus()) {
							outSetup(person);
							online.online.add(person);
//							outServerAll("0,1,,," + person.getHandle() + " joined\n");

						} else {
							outSeverMessage(person, "invalid_password");
						}
					} finally {

					}

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
		// System.out.println(answer);
		String[] split = answer.split(",");

		byte nameLength = Byte.parseByte(split[0]);
		byte passLength = Byte.parseByte(split[1]);
		int junkread = split[0].length() + split[1].length() + 2;

		p.setHandle(answer.substring(junkread, junkread + nameLength));

		String pass = answer.substring(junkread + nameLength, junkread + nameLength + passLength);

		if (serverType.equals("01") || serverType.equals("11")) {
		p.setStatus(checkPassword(pass));
		if (p.getStatus() == false) {
			System.out.println("Failed password attempt");
		}
		} else {p.setStatus(true);}
	}

	/**
	 * outSetup sends data based on first response to build on client side.
	 * 
	 * @param person
	 * @throws IOException
	 */
	private void outSetup(Person person) throws IOException {
		person.getOutput().write(serverName + ";" + hexColor + ";" + customBack + "\n");
		person.getOutput().flush();
	}

	/**
	 * outServerMessage sends direct messages from server to a user.
	 * 
	 * @param person
	 * @param mess
	 * @throws IOException
	 */
	private void outSeverMessage(Person person, String mess) throws IOException {
		person.getOutput().write(mess);
		person.getOutput().flush();
	}

	private void outServerAll(Message mess) {
		try {
			online.push(mess);
		} catch (IOException e) {
			System.out.println("Failed Server push\n");
			e.printStackTrace();
		}
	}
	
	private String createServerType() {
		if(handles == true) {
			if(passes == true) {
				return "11";
			}else {
				return "10";
			}
		} else {
			if (passes == true) {
				return "01";
			}else {
				return "00";
			}
		}
	}

	// \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\ALL BELOW ARE SET, GET, CHECK// METHODS/////////////////////////////////
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
	private boolean setPassword(String x) {
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
}