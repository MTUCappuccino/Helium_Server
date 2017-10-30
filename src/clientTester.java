import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class clientTester {

	public static void main(String[] args) {
		testLocalConnect(9090);
	}

	public static void testLocalConnect(int port) {

		try {
			Socket s = new Socket("localhost", port);
			BufferedReader input1 = new BufferedReader(new InputStreamReader(s.getInputStream()));

			incoming(s, input1);
			/* incoming paste */

			outgoing(s, "4,0,2upa,");
			/* outgoing paste */

			outgoing(s, messOUT("1;3;1;tester1;test").toString());

			incoming(s, input1);

			// s.close();
			while (true)
				;
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void incoming(Socket s, BufferedReader input1) throws IOException {
		

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

	private static void outgoing(Socket s, String sting) throws IOException {
		PrintWriter out = new PrintWriter(s.getOutputStream(), true);
		out.println(sting);
		out.flush();
	}
	
	private static Message messOUT(String mess) {
    	String[] unparsedSegments = mess.split(";");
    	
        Message.MessageType type = Message.MessageType.values()[Integer.parseInt(unparsedSegments[0])];
        int id = Integer.parseInt(unparsedSegments[1]);
        Message.ContentType contentType = Message.ContentType.values()[Integer.parseInt(unparsedSegments[2])];
        String sender = unparsedSegments[3];
        String userMessage = unparsedSegments[4];
        
        Message m = new Message(type, id, contentType, sender, System.currentTimeMillis(), userMessage);
        
        return m;
    }


}
