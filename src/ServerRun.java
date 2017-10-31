

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

/**
 * ServerRun Command line maker for launching a server
 * 
 * @author Lucas_Catron
 *
 */
public class ServerRun {

	static Server server;
	private static boolean runningServer = false;
	private static boolean on = true;

	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);
		Sysp("Commands: make, help, shutdown,");
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
	 */
	private static void Sysp(String x) {
		System.out.println(x);
	}

	/**
	 * takeCommand takes string input and decides appropriate action
	 */
	private static void takeCommand(String command, Scanner scanner) {
		switch (command) {
		case "help":
			Sysp("HELP: prints this message");
			Sysp("MAKE: will ask for inputs then make a server");
			break;
		case "make":
			if (!runningServer) {
				makeServer(scanner);
			} else {
				Sysp("Already running server");
			}
			break;
		case "shutdown":
			server.closeServer();
			on = false;
			break;
		default:
			Sysp("Invaild command");
			break;
		}
	}

	/**
	 * makeServer asks for inputs to make a required server, then launches join
	 * thread
	 */
	private static void makeServer(Scanner input) {
		boolean handles;
		boolean passes;
		String pass = "";
		
		//Gets port number
		Sysp("Port to open: ");
		int port = Integer.parseInt(input.nextLine());
		
		//Gets Server name
		Sysp("Server Name: ");
		String name = input.nextLine(); 
		
		//Asks about passwords
		Sysp("Password required entry?(Y/N): ");
		String answer = input.nextLine().toLowerCase(); 
		
		//Checking password answer
		if (answer.equals("y") || answer.equals("yes")) {
			passes = true;
			Sysp("Password: ");
			pass = input.nextLine(); 
		}else{
			passes =false;
		}
		
		//Asks about handles
		Sysp("Do users require handles?(Y/N): ");
		answer = input.nextLine().toLowerCase(); 
		
		//Checking handles answer
		if (answer.equals("y") || answer.equals("yes")) {
			handles = true;
		} else {handles = false;}

		//Checks given port availability
		while (checkPortInUse(port)) {
			Sysp("Port in use. AutoPort? Y/N : ");
			answer = input.nextLine().toLowerCase(); 
			if (answer.equals("y") || answer.equals("yes")) {
				port = autoPort(port);
				if (port == -1) {break;}
				Sysp("Now using port: " + port);
			} else {
				System.out.println("New port: ");
				port = Integer.parseInt(input.nextLine());
			}
		}
		
		//Creates new server with given inputs
		server = new Server(port, name, pass, passes, handles);
		
		//sets color of server
		setColor(input);

		//opens that server
		server.openServer();
		runningServer = true;
	}

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

	private static int autoPort(int port) {

		while (checkPortInUse(port)) {
			port += 1;
			if (port >= 9000) {return -1;}
		}
		return port;
	}
	
	private static void setColor(Scanner input) {
		String color ="";
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
		}else {
			Sysp("Would you like to enter 6 character hex format color code?(Y/N): ");
			answer = input.nextLine().toLowerCase();
			if (answer.equals("y") || answer.equals("yes")) {
				Sysp("Input: ");
				color = input.nextLine().toLowerCase();
				if (color.length() < 6) {
					Sysp("Invaild color format: Setting color to default");
					color = "000000";
				}
			}
		}
		server.setHexColor(color);
	}

}