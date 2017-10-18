import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Server 
 * the Sever for Helium
 * 
 * @author Lucas_Catron
 *
 */
public class Server implements Runnable {

	private boolean open;
	private int port;
	private ServerSocket listener;
	private String serverName = "";
	private String password = "";
	private String hexColor = "000000";
	private String customBack = "";
	
	public boolean public_;
	
	/**
	 * Server 
	 * 
	 * Constructor; sets the port number, and name, and password
	 * @param portNum number to set to
	 */
	Server(int portNum, String name, String pass) {
		setPort(portNum);
		setName(name);
		setPassword(pass);
	}

	/**
	 * openServer
	 * opens new server
	 * @return
	 */
	public boolean openServer() {
		(new Thread(new Server(port, serverName, password))).start();
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
						initalOut(person.getSocket());
						initalIn(person.getSocket(), person);

						if(person.getStatus()) {
							outSetup(person.getSocket());
						}
						else {outSeverMessage(person.getSocket(), "invalid_password");}
					} finally {
//						socket.close();
					}

				}

			} finally {
				listener.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * initialOut
	 * place holder for future first contact with client
	 * @param socket
	 * @throws IOException
	 */
	private void initalOut(Socket socket) throws IOException {
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		out.println("10");
		out.flush();
		//out.close();
	}

	/**
	 * initalIn
	 * This method MUST run until input receive or time out. Takes response from client. Place holder
	 * @param socket
	 * @return
	 * @throws IOException
	 */
	private void initalIn(Socket socket, Person p) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		boolean t = true;
		while (t) {
			if (in.ready()) {
				String answer = in.readLine();
				System.out.println(answer);
				String[] split = answer.split(",");
				
				byte nameLength = Byte.parseByte(split[0]);
				byte passLength = Byte.parseByte(split[1]);
				int junkread = split[0].length() + split[1].length() + 2;
				
				p.setHandle(answer.substring(junkread, junkread + nameLength));
				
				String pass = answer.substring(junkread + nameLength, junkread + nameLength + passLength);
				
				System.out.println(pass);
//				System.out.println(checkPassword(pass)); 
				p.setStatus(checkPassword(pass));
				t = false;
			} else {

			}
		}
//		in.close();
	}
	
	
	
	private void outSetup(Socket socket) throws IOException {
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		out.println(serverName + ";" + hexColor + ";" + customBack);
		out.flush();
		//out.close();
	}
	
	private void outSeverMessage(Socket socket, String mess) throws IOException {
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		out.println(mess);
		out.flush();
		//out.close();
	}
	//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\ALL BELOW ARE SET, GET, CHECK METHODS/////////////////////////////////
	/**
	 * setPort
	 * @param num
	 */
	public void setPort(int num) {
		port = num;
	}

	/**
	 * getPort
	 * @return the port number
	 */
	public int getPort() {
		return port;
	}
	
	public void setName(String nim){
		serverName = nim;
	}
	
	public String getName(){
		return serverName;
	}
	
	private boolean setPassword(String x) {
		password = x;
		return true;
	}
	
	private boolean checkPassword(String x) {
		if (x.equals(password)) {
			return true;
		}
		else { return false;}
	}
	
	public void setHexColor(String x) {
		hexColor = x;
	}

}
