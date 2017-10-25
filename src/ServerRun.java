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
			if (input.hasNext()) {
				String command = input.next().toLowerCase();
				takeCommand(command);
			}
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
	 * makeServer asks for inputs to make a required server, then launches join
	 * thread
	 */
	private static void makeServer() {
		String pass = "";
		Scanner input = new Scanner(System.in);
		Sysp("Port to open: ");
		int port = input.nextInt();
		Sysp("Server Name: ");
		String name = input.next();
		Sysp("Password entry?(Y/N): ");
		String answer = input.next().toLowerCase();
		if (answer.equals("y") || answer.equals("yes")) {
			Sysp("Password: ");
			pass = input.next();
		}
		server = new Server(port, name, pass);
		input.close();
		server.openServer();
		runningServer = true;
	}

	/**
	 * takeCommand takes string input and decides appropriate action
	 */
	private static void takeCommand(String command) {
		switch (command) {
		case "help":
			Sysp("MAKE: will ask for inputs then make a server");
			Sysp("HELP: prints this message");
			// takeCommand();
			break;
		case "make":
			if (!runningServer)
				makeServer();
			break;
		case "shutdown":
			on = false;
			break;
		default:
			Sysp("Invaild command");
			break;
			// takeCommand();
		}
	}

}