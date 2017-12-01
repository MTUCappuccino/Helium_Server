import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

public class clientTester {

	public static String t1;
	
	static Message m = new Message(Message.MessageType.NEW_MESSAGE,
			Message.ContentType.TEXT, "Client1", 
			("READ ME\n"));
	
//	public clientTester(int port) {
//		t1 =testLocalConnect(port);
//	}
	
	public static void main(String[] args) {
		t1 = testLocalConnect(1024);
		System.out.println(t1);
	}

	
	public static String testLocalConnect(int port) {

		try {
			Socket s = new Socket("localhost", port);
			Person client = new Person(s);

			incoming(s, client.getInput());
			/* incoming paste */

			outgoing(client.getOutput(), "4,6,2upa3DK5R8"); //4,4,2upaMega\n //0,0,
			/* outgoing paste */

			
//			outgoing(client.getOutput(), m.toString());
			int count = 0;
			while(count < 50) {
				System.out.println(incoming(s, client.getInput()));
				count++;
			}

			return incoming(s, client.getInput());
			// s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String incoming(Socket s, BufferedReader input1) throws IOException {
//		boolean t = true;
//
//		while (t) {
//			if (input1.ready()) {
				String answer = input1.readLine();
				return answer;
//			} else {
//			}
//		}
//		return null;

	}

	private static void outgoing(BufferedWriter out, String sting) throws IOException {
		out.write(sting + "\n");
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
