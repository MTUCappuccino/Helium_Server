import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
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

	// Port to run on
	private int port;
	private int defaultPort = 9090;
	private boolean autoPort = false;
	private ServerSocket listener;
	private String serverName = "";
	private String password = "";
	private String hexColor = "000000";
	private String customBack = "NULL";

	public boolean public_;
	Thread t = new Thread(this);
	ServerMessaging online = new ServerMessaging();

	/**
	 * Server
	 *
	 * Constructor; sets the port number, and name, and password
	 *
	 * @param portNum
	 *            number to set to
	 */
	Server(int portNum, String name, String pass) {
		setPort(portNum);
		setName(name);
		setPassword(pass);
	}

	/**
	 * openServer opens new server
	 *
	 * @return
	 */
	public boolean openServer() {
		new Thread(this).start();
		return true;
	}

	/**
	 * closeServer()
	 *
	 * @return boolean, successful close = true
	 */
	public boolean closeServer() {
		open = false;
		return true;

	}

	@Override
	public void run() {
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
							outServerAll("0,1,,," + person.getHandle() + " joined\n");
							// System.out.println(online.online.size());

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
			System.out.println("Port in use already.");
			if (autoPort == false) {
				
				Scanner input = new Scanner(System.in);
				System.out.println("Would you like to try to auto port? Y/N : ");
				while (input.hasNext() == false) {
//					NEEDS TO WAIT FOR INPUT
				}
				String answer = input.next().toLowerCase();
				
				if (answer == "y") {
					autoPort = true;
				} else {
					System.out.println("Port: ");
					int p = input.nextInt();
					setPort(p);
				}
				
			} else {
				if (defaultPort == port) {
					defaultPort += 1;
				}
				setPort(defaultPort);
				System.out.println("Trying: " + defaultPort);
				defaultPort += 1;
			}

//			setPort(9090);
			run();
			// e.printStackTrace();
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
		person.getOutput().write("10\n");
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

		p.setStatus(checkPassword(pass));
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

	private void outServerAll(String mess) {
		try {
			online.push(mess);
		} catch (IOException e) {
			System.out.println("Failed Server push\n");
			e.printStackTrace();
		}
	}

	// \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\ALL BELOW ARE SET, GET, CHECK
	// METHODS/////////////////////////////////
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