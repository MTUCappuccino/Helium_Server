

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
		String pass = "";
		Sysp("Port to open: ");
		int port = Integer.parseInt(input.nextLine());
		Sysp("Server Name: ");
		String name = input.nextLine(); // USE NEXT LINE
		Sysp("Password entry?(Y/N): ");
		String answer = input.nextLine().toLowerCase(); // USE NEXT LINE
		if (answer.equals("y") || answer.equals("yes")) {
			Sysp("Password: ");
			pass = input.nextLine(); // USE NEXT LINE
		}

		while (checkPortInUse(port)) {
			Sysp("Port in use. AutoPort? Y/N : ");
			answer = input.nextLine().toLowerCase(); // USE NEXT LINE
			if (answer.equals("y") || answer.equals("yes")) {
				port = autoPort(port);
				Sysp("Now using port: " + port);
			} else {
				System.out.println("New port: ");
				port = Integer.parseInt(input.nextLine());
			}
		}
		
		
		server = new Server(port, name, pass);

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
		}
		return port;
	}

}