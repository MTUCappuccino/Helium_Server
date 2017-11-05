import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


import org.junit.Test;

public class serverTEST {

	Server test00;
	Server test11;


	@Test
	public void RunTest1() {
		test00 = new Server(9090, "TEST", "", false, false);
		test00.openServer();
	}
	
	@Test
	public void connectTest1() {
		String cTest1 = testLocalConnect(9090, "", "");
		
		if(!cTest1.equals("TEST;000000;NULL")) {
			fail();
		}
	}
	
	
	@Test
	public void RunTest2() {
		test11 = new Server(9091, "TEST1", "Mega", true, true);
		test11.openServer();
	}
	@Test
	public void connectTest2() {
		String cTest2 = testLocalConnect(9091, "2upa", "Mega");
		
		if(!cTest2.equals("TEST1;000000;NULL")) {
			fail();
		}
	}
	
	@Test
	public void RunTest3() {
		test11 = new Server(9092, "TEST1", "HugeHugePassword of doom12", true, true);
		test11.setHexColor("ffffff");
		test11.setIconURL("lolcat.com");
		test11.openServer();
	}
	@Test
	public void connectTest3() {
		String cTest2 = testLocalConnect(9092, "2upa", "HugeHugePassword of doom12");
		
		if(!cTest2.equals("TEST1;ffffff;lolcat.com")) {
			fail();
		}
	}
	
	
	
	public static String testLocalConnect(int port, String handle, String pass) {

		try {
			Socket s = new Socket("localhost", port);
			BufferedReader input1 = new BufferedReader(new InputStreamReader(s.getInputStream()));

			incoming(s, input1);
			/* incoming paste */

			outgoing(s, handle.length()+","+pass.length()+","+handle+","+pass+"\n"); //4,4,2upa,Mega\n
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
