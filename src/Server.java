import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Server Abstract of the Sever for Helium
 * 
 * @author Kiarimas
 *
 */
public class Server implements Runnable {

	boolean open;
	private int port;
	private ServerSocket listener;

	Server(int portNum) {
		setPort(portNum);
	}

	public boolean openServer() {
		try {
			listener = new ServerSocket(port);
			open = true;
			try {
				while (true) {
					Socket socket = listener.accept();
					try {
						PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
						out.println("Hello");
						out.println();
						out.println("This is a server");
						out.flush();
					} finally {
						socket.close();
					}

				}

			} finally {
				listener.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * closeServer()
	 * 
	 * @return boolean, successful close = true
	 */
	public boolean closeServer() {
		try {
			listener.close();
			open = false;
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	public void setPort(int num) {
		port = num;
	}

	public int getPort() {
		return port;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
