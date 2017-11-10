
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

/**
 * ServerRun Command line maker for launching a server Make server, returns some
 * stats, some admin commands
 * 
 * @author Lucas_Catron
 *
 */
public class ServerRun {

	// Global tracking variables
	static Server server;
	private static boolean runningServer = false;
	private static boolean on = true;

	// Runs this thread until shutdown
	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);
		Sysp("Commands: close, make, help, shutdown, edit...");
		while (on) {
			System.out.print("> "); // ADDED A LITTLE PROMPT
			String command = input.nextLine().toLowerCase();
			takeCommand(command, input);
			// }
		}

	}

	/**
	 * Sysp System print short cut statement
	 * 
	 * @param x
	 *            String to print
	 */
	private static void Sysp(String x) {
		System.out.println(x);
	}

	/**
	 * takeCommand takes string input and decides appropriate action
	 * 
	 * @param command
	 *            Sting to compare in switch statement
	 * @param scanner
	 *            Scanner to pass down for further inputs
	 */
	private static void takeCommand(String command, Scanner scanner) {
		switch (command) {
		case "help":
			Sysp("HELP: prints this message");
			Sysp("MAKE: will ask for inputs then make a server");
			Sysp("EDIT: if a server is running allow you to update settings");
			Sysp("SHUTDOWN: closes server and your server software");
			Sysp("STATS: prints some stats about your server if running");
			Sysp("CLOSE: Closes the current server running");
			break;
		case "make":
			if (!runningServer) {
				makeServer(scanner);
			} else {
				Sysp("Already running server");
			}
			break;
		case "shutdown":
			if (runningServer) {
				server.closeServer();
				runningServer = false;
			}
			on = false;
			break;
		case "close":
			if (runningServer) {
				server.closeServer();
				runningServer = false;
				Sysp("Closed Server");
			}
			break;
		case "edit":
			edit(scanner);
			break;
		case "stats":
			stats();
			break;
		default:
			Sysp("Invaild command");
			break;
		}
	}

	/**
	 * makeServer asks for inputs to make a required server, then launches join
	 * thread
	 * 
	 * @param input
	 *            Scanner for further inputs
	 */
	private static void makeServer(Scanner input) {
		boolean handles;
		boolean passes;
		String pass = "";

		// Gets port number
		
		int port = setPort(input);

		// Gets Server name
		Sysp("Server Name: ");
		String name = input.nextLine();

		// Asks about passwords
		Sysp("Password required entry?(Y/N): ");
		String answer = input.nextLine().toLowerCase();

		// Checking password answer
		if (answer.equals("y") || answer.equals("yes")) {
			passes = true;
			Sysp("Password: ");
			pass = input.nextLine();
		} else {
			passes = false;
		}

		// Asks about handles
		Sysp("Do users require handles?(Y/N): ");
		answer = input.nextLine().toLowerCase();

		// Checking handles answer
		if (answer.equals("y") || answer.equals("yes")) {
			handles = true;
		} else {
			handles = false;
		}

		// Checks given port availability

	

	// Creates new server with given inputs
	server = new Server(port,name,pass,passes,handles);

	// sets color of server
	setColor(input);

		// set Icon
		Sysp("Set custom Icon with URL? (Y/N): ");
		answer = input.nextLine().toLowerCase();

		// Checking Icon answer
		if (answer.equals("y") || answer.equals("yes")) {
			Sysp("Enter URL of Icon: ");
			String URL = input.nextLine();
			if (!URL.equals(""))
				server.setIconURL(URL);
		}

		// opens that server
		server.openServer();
		runningServer = true;
	}

	/**
	 * checkPortInUse checks to see if the port is in use
	 * 
	 * @param port
	 *            Int of port
	 * @return true if cannot access for any reason, false if can accsess and
	 *         close
	 */
	private static boolean checkPortInUse(int port) {
		ServerSocket listener;

		try {
			listener = new ServerSocket(port);
			listener.close();
			return false;
		} catch (IOException e) {
			return true;
		}
	}

	/**
	 * edit if the server is running allows editing of some configurable options
	 * 
	 * @param input
	 *            Scanner to allow for further input
	 */
	private static void edit(Scanner input) {
		if (!runningServer) {
			Sysp("No server running to edit...");
			return;
		}

		Sysp("What do you want to edit: Color, Icon, Name");
		String answer = input.nextLine().toLowerCase();

		switch (answer) {
		case "color":
			setColor(input);
			server.update();
			break;
		case "icon":
			Sysp("Enter URL of Icon: ");
			String URL = input.nextLine();
			server.setIconURL(URL);
			server.update();
			break;
		case "name":
			Sysp("Enter new server name: ");
			String name = input.nextLine();
			server.setName(name);
			server.update();
			break;
		default:
			Sysp("Invaild command");
		}

	}

	/**
	 * autoPort (UNSAFE) start at specified port, increases by 1, until a port
	 * that is open is found.
	 * 
	 * @param port
	 *            INT of port to try from.
	 * @return open port number of negative 1 for failure.
	 */
	private static int autoPort(int port) {

		while (checkPortInUse(port)) {
			port += 1;
			if (port >= 9000) {
				return -1;
			}
		}
		return port;
	}
	
	/**
	 * setPort
	 * makes sure ports are valid and does cause huge probs
	 * @param input
	 * @return
	 */
	public static int setPort(Scanner input) {
		String answer;
		String s;
		int port = -1;
		
		do {
			Sysp("Port to open: ");
			s = input.nextLine();
			port = inCheckINT(s);
		}while (port == -1);
		
		while (checkPortInUse(port)) {
			Sysp("Port in use. AutoPort? Y/N : ");
			answer = input.nextLine().toLowerCase();
			if (answer.equals("y") || answer.equals("yes")) {
				port = autoPort(port);

				if (port == -1) {
					// on = false;
					Sysp("Failed to find port...");
					break;
				}
				Sysp("Now using port: " + port);
			} 
			else {
				do {
					Sysp("New Port to open: ");
					s = input.nextLine();
					port = inCheckINT(s);
				}while (port == -1);
			}
		}
		return port;
	}
	
	/**
	 * inCheckINT
	 * checks that given string contains only chars that are numbers.
	 * @param s
	 * @return
	 */
	private static int inCheckINT(String s) {
		int port = -1;
		
		boolean t = true;
		for (int i = 0; i < s.length(); i++) {
			if (48 > s.charAt(i) || s.charAt(i) > 57) {
				Sysp("Invaild port");
				t = false;
				break;
			}
		}
		if (t == true)
			port = Integer.parseInt(s);
		return port;
	}

	/**
	 * setColor lets you set clients colors. First asks if want to choose from
	 * preselected colors then if you want to specify you own hexadecimal color.
	 * 
	 * @param input
	 */
	private static void setColor(Scanner input) {
		String color = "";
		String answer;

		Sysp("Select a preset server color(DEFALUT:BLACK)?(Y/N): ");
		answer = input.nextLine().toLowerCase();

		if (answer.equals("y") || answer.equals("yes")) {
			Sysp("Colors: RED, BLUE, GREEN, ORANGE, PURPLE, BROWN, YELLOW, WHITE");
			answer = input.nextLine().toLowerCase();

			switch (answer) {
			case "red":
				color = "b03a2e";
				break;
			case "blue":
				color = "1f618d";
				break;
			case "green":
				color = "1e8449";
				break;
			case "orange":
				color = "d35400";
				break;
			case "purple":
				color = "6c3483";
				break;
			case "brown":
				color = "9d8c66";
				break;
			case "yellow":
				color = "f4d03f";
				break;
			case "white":
				color = "ffffff";
				break;
			default:
				color = "000000";
			}
		} else {
			Sysp("Would you like to enter 6 character hex format color code?(Y/N): ");
			answer = input.nextLine().toLowerCase();
			if (answer.equals("y") || answer.equals("yes")) {
				Sysp("Input: ");
				color = input.nextLine().toLowerCase();
				if (color.length() < 6) {
					Sysp("Invaild color format: Setting color to default");
					color = "000000";
				}
			} else {
				color = "000000";
			}
		}
		server.setHexColor(color);
	}

	/**
	 * stats prints stats about the server
	 */
	private static void stats() {
		if (!runningServer) {
			Sysp("No server running to get info about...");
			return;
		}
		Sysp("Number of users that are online: " + server.messaging.online.size());
		Sysp("Number of messages sent: " + server.getMessagesSent());
		Sysp("Number of messages edited: " + server.getMessEdited());
	}

}