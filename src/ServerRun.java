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
//			if (input.hasNext()) { NO NEED FOR THIS, nextLine() WILL BLOCK
				String command = input.nextLine().toLowerCase();
				takeCommand(command, input);
//			}
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
	private static void makeServer(Scanner input) {
		String pass = "";
//		Scanner input = new Scanner(System.in); DON'T CREATE A NEW SCANNER
		Sysp("Port to open: ");
		int port = input.nextInt();
		Sysp("Server Name: ");
		String name = input.nextLine(); // USE NEXT LINE
		Sysp("Password entry?(Y/N): ");
		String answer = input.nextLine().toLowerCase(); // USE NEXT LINE
		if (answer.equals("y") || answer.equals("yes")) {
			Sysp("Password: ");
			pass = input.nextLine(); // USE NEXT LINE
		}
		server = new Server(port, name, pass);
//		input.close(); SINCE WE'RE SHARING A SCANNER, DON'T CLOSE IT!
		server.openServer();
		runningServer = true;
	}

	/**
	 * takeCommand takes string input and decides appropriate action
	 */
	private static void takeCommand(String command, Scanner scanner) {
		switch (command) {
		case "help":
			Sysp("MAKE: will ask for inputs then make a server");
			Sysp("HELP: prints this message");
			// takeCommand();
			break;
		case "make":
			if (!runningServer)
				makeServer(scanner);
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