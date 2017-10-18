import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ServerMessaging implements Runnable {

	ArrayList<Person> online = new ArrayList<Person>();
	
	private boolean open = true;
	
	public boolean openMess() {
		(new Thread(new ServerMessaging())).start();
		return true;
	}
	
	@Override
	public void run() {
		while (open) {
			for(int i = 0; i < online.size(); i++) {
				try {
					BufferedReader in = new BufferedReader(new InputStreamReader(online.get(i).getSocket().getInputStream()));
					if (in.ready()) {
						String mess = in.readLine();
						push(mess);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	protected void push(String message) throws IOException {
		System.out.println("PUSHING");
		for(int i = 0; i < online.size(); i++) {
			PrintWriter out = new PrintWriter(online.get(i).getSocket().getOutputStream(), true);
			out.println(message);
			out.flush();
		}
	}

}
