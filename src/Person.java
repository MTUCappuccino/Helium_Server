import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Person
 * @author Lucas_Catron
 * temp idea to store the different clients data.
 */
public class Person {


	private boolean good = true;
	private String handle;
	private Socket socket;
        private BufferedReader input;
        private BufferedWriter output;
	
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
            try {
                socket = s;
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            } catch (IOException ex) {
                Logger.getLogger(Person.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
	
	public void setStatus(boolean b) {
		good = b;
	}
	
	public boolean getStatus() {
		return good;
	}

    public BufferedReader getInput() {
        return input;
    }

    public BufferedWriter getOutput() {
        return output;
    }
        
        
}
