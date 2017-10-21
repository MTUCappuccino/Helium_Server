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
 * used to store the different clients data and pass between accepting and messaging
 */
public class Person {

	//Checks if password entered correctly
	private boolean good = true;
	
	//Name of user
	private String handle;
	
	//Data for io
	private Socket socket;
        private BufferedReader input;
        private BufferedWriter output;
	
        /**
         * setHandle
         * sets this persons name
         * @param name = new handle
         */
	public void setHandle(String name) {
		handle = name;
	}
	
	/**
	 * getHandle
	 * gets the handle
	 * @return this persons handle
	 */
	public String getHandle(){
		return handle;
	}
	
	/**
	 * getSocket
	 * @return this persons socket
	 */
	public Socket getSocket() {
		return socket;
	}
	
	/**
	 * Constructor
	 * builds a person
	 * @param s the socket they connected in on
	 */
	public Person(Socket s) {
            try {
                socket = s;
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            } catch (IOException ex) {
                Logger.getLogger(Person.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
	
	/**
	 * setStatus
	 * sets this persons status
	 * @param b
	 */
	public void setStatus(boolean b) {
		good = b;
	}
	
	/**
	 * getStatus
	 * returns this persons status
	 * @return
	 */
	public boolean getStatus() {
		return good;
	}

	/**
	 * getInput
	 * returns this person input stream
	 * @return BufferedReader
	 */
    public BufferedReader getInput() {
        return input;
    }

    /**
     * getOutPut
     * returns this persons output stream
     * @return BufferedWriter
     */
    public BufferedWriter getOutput() {
        return output;
    }
        
        
}