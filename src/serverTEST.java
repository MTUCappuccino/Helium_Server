import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


import org.junit.Test;

public class serverTEST {

	@Test
	public void buildTest() {
		Server test = new Server(9090, "TEST", "", false, true);
	}

	@Test
	public void RunTest1() {
		Server test = new Server(9090, "TEST", "", false, true);
		test.openServer();
		test.closeServer();
	}
	
	@Test
	public void connectTest() {

		assertEquals("TEST;000000;NULL", serverTEST.test(9090));
	}
	
	public static String test(int port) {

		String answer = "";
		try {
			Socket s = new Socket("localhost", port);

			incoming(s);
			/* incoming paste */

			outgoing(s);
			/* outgoing paste */

			answer = incoming(s);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return answer;
	}
	
	private static String incoming(Socket s) throws IOException {
		BufferedReader input1 = new BufferedReader(new InputStreamReader(s.getInputStream()));

		boolean t = true;

		while (t) {
			if (input1.ready()) {
				String answer = input1.readLine();
				return answer;
			} else {
			}
		}
		return "";

	}

	private static void outgoing(Socket s) throws IOException {
		PrintWriter out = new PrintWriter(s.getOutputStream(), true);
		out.println("4,0,2upa,");
		out.flush();
	}
	
}
