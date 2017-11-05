import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class clientTester {

	public String t1;
	
	public clientTester(int port) {
		t1 =testLocalConnect(port);
	}
	
//	public static void main(String[] args) {
//		t1 = testLocalConnect(9090);
//		System.out.println(t1);
//	}

	
	public static String testLocalConnect(int port) {

		try {
			Socket s = new Socket("localhost", port);
			BufferedReader input1 = new BufferedReader(new InputStreamReader(s.getInputStream()));

			incoming(s, input1);
			/* incoming paste */

			outgoing(s, "0,0,,\n"); //4,4,2upa,Mega\n
			/* outgoing paste */

//			outgoing(s, messOUT("1;3;1;tester1;test").toString());

			return incoming(s, input1);
			// s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String incoming(Socket s, BufferedReader input1) throws IOException {
		

		boolean t = true;

		while (t) {
			if (input1.ready()) {
				String answer = input1.readLine();
				return answer;
			} else {
			}
		}
		return null;

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
