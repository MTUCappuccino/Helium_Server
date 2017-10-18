import java.util.Scanner;
/**
 * ServerRun
 * Command line maker for launching a server
 * @author Lucas_Catron
 *
 */
public class ServerRun {
	static Server server;

	public static void main(String[] args) {

		//Commands input area
		Sysp("Commands: make, help, ,");
		takeCommand();

	}
	
	/**
	 * Sysp 
	 * System print short cut statement
	 * @param x
	 */
	private static void Sysp(String x) {
		System.out.println(x);
	}
	
	/**
	 * makeServer
	 * asks for inputs to make a required server, then launches join thread
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
		if(answer.equals("y")||answer.equals("yes")) {
			Sysp("Password: ");
			pass = input.next();
		}
		server = new Server(port, name, pass);
		input.close();
		server.openServer();
	}
	
	/**
	 * takeCommand
	 * takes string input and decides appropriate action
	 */
	private static void takeCommand(){
		Scanner input = new Scanner(System.in);
		String command = input.next().toLowerCase();
		switch (command) {
		case "help": 
			Sysp("MAKE: will ask for inputs then make a server");
			Sysp("HELP: prints this message");
			takeCommand();
			break;
		case "make":  
			makeServer();
			break;
		default: 
			Sysp("Invaild command");
			input.close();
			takeCommand();
		}
	}

}
