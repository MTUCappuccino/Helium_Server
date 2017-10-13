import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class clientTester {

	public static void main(String[] args) {
		test(9090);
	}

	public static void test(int port) {
		Scanner input = new Scanner(System.in);
		System.out.println("Enter IP Address port:");
		String serverAddress = input.nextLine();
		input.close();

		try {
			Socket s = new Socket(serverAddress, port);
			BufferedReader input1 = new BufferedReader(new InputStreamReader(s.getInputStream()));

//			while (input1.ready()) {
				String answer = input1.readLine();
				System.out.println(answer);
			//}
//			s.close();
				while(true);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
