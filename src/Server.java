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
 * @author Lucas_Catron
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
		(new Thread(new Server(port))).start();
		return true;
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
		try {
			listener = new ServerSocket(port);
			open = true;
			try {
				while (true) {
					System.out.println("Waiting for a client...");
				    Socket socket = listener.accept();
				    System.out.println("accepted the socket");
				    System.out.println();
					try {
						PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
						out.println("10");
						out.flush();

						BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						boolean t = true;
						while (t) {
							if (br.ready()) {
								String answer = br.readLine();
								System.out.println(answer);
							} else {
								t = false;
							}
						}

					} finally {
						socket.close();
					}

				}

			} finally {
				listener.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
