import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerMessaging implements Runnable {

    ArrayList<Person> online = new ArrayList<Person>();

    private boolean open = true;

    public ServerMessaging() {
        System.out.println("STARTING THREAD");
        Thread t = new Thread(this);
        t.setName("ServerMessaging");
        t.start();
    }

    @Override
    public void run() {
        while (open) {
            for (int i = 0; i < online.size(); i++) {
                try {
                    if (online.get(i).getInput().ready()) {
                        String mess = online.get(i).getInput().readLine();
                        System.out.println("Got message from " + online.get(i).getHandle() + ": " + mess);
                        push(mess);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                Logger.getLogger(ServerMessaging.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * push
     * pushes message to all list persons currently joined
     * @param message
     * @throws IOException
     */
    protected void push(String message) throws IOException {
        System.out.println("PUSHING");
        for (int i = 0; i < online.size(); i++) {
            System.out.println("Sending to: " + online.get(i).getHandle());
            online.get(i).getOutput().write(message + "\n");
            online.get(i).getOutput().flush();
        }
    }
}