import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class clientTester {

	public static void main(String[] args) {
		test(9090);
	}

	public static void test(int port) {
		Scanner input = new Scanner(System.in);
		System.out.println("Enter IP Address:");
		String serverAddress = input.nextLine();
		input.close();

		try {
			Socket s = new Socket(serverAddress, port);
			
			while(incoming(s));
			/*incoming paste*/
			
			outgoing(s);
			/*outgoing paste*/
			
			
//			s.close();
				while(true);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private static boolean incoming(Socket s) throws IOException {
		BufferedReader input1 = new BufferedReader(new InputStreamReader(s.getInputStream()));
		
		boolean t = true;
		boolean wait = true;
		
		while (t) {
		if (input1.ready()) {
			String answer = input1.readLine();
			System.out.println(answer);
			wait = false;
		}
		else {t = false;}
		}
		return wait;
	}
	
	private static void outgoing(Socket s) throws IOException {
		PrintWriter out = new PrintWriter(s.getOutputStream(), true);
		out.println("Hello");
		out.println("This is a client");
		out.println();
		out.flush();
	}

}
