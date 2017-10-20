
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

/**
 * Server the Sever for Helium
 *
 * @author Lucas_Catron
 *
 */
public class Server implements Runnable {

    private boolean open;
    private int port;
    private ServerSocket listener;
    private String serverName = "";
    private String password = "";
    private String hexColor = "000000";
    private String customBack = "NULL";

    public boolean public_;
    Thread join;
    ServerMessaging online = new ServerMessaging();

    /**
     * Server
     *
     * Constructor; sets the port number, and name, and password
     *
     * @param portNum number to set to
     */
    Server(int portNum, String name, String pass) {
        setPort(portNum);
        setName(name);
        setPassword(pass);
    }

    /**
     * openServer opens new server
     *
     * @return
     */
    public boolean openServer() {
        new Thread(this).start();
        return true;
    }

    /**
     * closeServer()
     *
     * @return boolean, successful close = true
     */
    public boolean closeServer() {
        open = false;
        return true;

    }

    @Override
    public void run() {
        try {
            listener = new ServerSocket(port);
            open = true;
            try {
                while (open) {
                    System.out.println("Waiting for a client...");
                    Socket socket = listener.accept();
                    System.out.println("Accepted a socket...");
                    System.out.println();
                    try {
                        Person person = new Person(socket);
                        initalOut(person);
                        initalIn(person);

                        if (person.getStatus()) {
                            outSetup(person);
                            online.online.add(person);
                            System.out.println(online.online.size());

                        } else {
                            outSeverMessage(person, "invalid_password");
                        }
                    } finally {

                    }

                }

            } finally {
                listener.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * initialOut place holder for future first contact with client
     *
     * @param socket
     * @throws IOException
     */
    private void initalOut(Person person) throws IOException {
        person.getOutput().write("10\n");
        person.getOutput().flush();
        // out.close();
    }

    /**
     * initalIn This method MUST run until input receive or time out. Takes
     * response from client. Place holder
     *
     * @param socket
     * @return
     * @throws IOException
     */
    private void initalIn(Person p) throws IOException {
        boolean t = true;
//		while (t) {
//			if (in.ready()) {
        String answer = p.getInput().readLine();
//				System.out.println(answer);
        String[] split = answer.split(",");

        byte nameLength = Byte.parseByte(split[0]);
        byte passLength = Byte.parseByte(split[1]);
        int junkread = split[0].length() + split[1].length() + 2;

        p.setHandle(answer.substring(junkread, junkread + nameLength));

        String pass = answer.substring(junkread + nameLength, junkread + nameLength + passLength);

//				System.out.println(pass);
        // System.out.println(checkPassword(pass));
        p.setStatus(checkPassword(pass));
        t = false;
//			} else {
//
//			}
//		}
        // in.close();
    }

    private void outSetup(Person person) throws IOException {
        person.getOutput().write(serverName + ";" + hexColor + ";" + customBack + "\n");
        person.getOutput().flush();
    }

    private void outSeverMessage(Person person, String mess) throws IOException {
        person.getOutput().write(mess);
        person.getOutput().flush();
        // out.close();
    }

    // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\ALL BELOW ARE SET, GET, CHECK METHODS/////////////////////////////////
    /**
     * setPort
     *
     * @param num
     */
    public void setPort(int num) {
        port = num;
    }

    /**
     * getPort
     *
     * @return the port number
     */
    public int getPort() {
        return port;
    }

    public void setName(String nim) {
        serverName = nim;
    }

    public String getName() {
        return serverName;
    }

    private boolean setPassword(String x) {
        password = x;
        return true;
    }

    private boolean checkPassword(String x) {
        return x.equals(password);
    }

    public void setHexColor(String x) {
        hexColor = x;
    }
}
