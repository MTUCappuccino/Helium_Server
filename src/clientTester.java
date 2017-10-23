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

			incoming(s);
			/* incoming paste */

			outgoing(s);
			/* outgoing paste */

			incoming(s);

			incoming(s);

			// s.close();
			while (true)
				;
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void incoming(Socket s) throws IOException {
		BufferedReader input1 = new BufferedReader(new InputStreamReader(s.getInputStream()));

		boolean t = true;

		while (t) {
			if (input1.ready()) {
				String answer = input1.readLine();
				System.out.println(answer);
				t = false;
			} else {
			}
		}

	}

	private static void outgoing(Socket s) throws IOException {
		PrintWriter out = new PrintWriter(s.getOutputStream(), true);
		out.println("4,0,2upa,");
		out.flush();
	}


}
