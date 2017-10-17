import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Person
 * @author Lucas_Catron
 * temp idea to store the different clients data.
 */
public class Person {

	private PrintWriter out;
	private BufferedReader in;
	private String handle;
	
	public void setIn(BufferedReader incoming) {
		in = incoming;
	}
	
	public void setOut(PrintWriter outgoing) {
		out = outgoing;
	}
	
	public void setHandle(String name) {
		handle = name;
	}
	
	public BufferedReader getIn(){
		return in;
	}
	
	public PrintWriter getOut(){
		return out;
	}
	
	public String getHandle(){
		return handle;
	}
}
