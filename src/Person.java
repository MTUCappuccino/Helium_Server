import java.net.Socket;

/**
 * Person
 * @author Lucas_Catron
 * temp idea to store the different clients data.
 */
public class Person {


	private String handle;
	private Socket socket;

	
	public void setHandle(String name) {
		handle = name;
	}
	
	public String getHandle(){
		return handle;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public Person(Socket s) {
		socket = s;
	}
}
