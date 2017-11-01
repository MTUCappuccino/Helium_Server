
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;



public class ServerMessaging implements Runnable {

    ArrayList<Person> online = new ArrayList<Person>();
    protected CircleQue messQueue = new CircleQue(300);
    
//    Message zero = new Message(Message.MessageType.NEW_MESSAGE, null, null, null);
//    Message one = new Message(Message.MessageType.EDIT_MESSAGE, null, null, null);
//    Message two = new Message(Message.MessageType.DELETE_MESSAGE, null, null, null);
//    Message three = new Message(Message.MessageType.CLOSE_CONNECTION, null, null, null);
//    Message four = new Message(Message.MessageType.LEAVE_SERVER, null, null, null);
//    Message five = new Message(Message.MessageType.UPDATE_SERVER_DATA, null, null, null);

    private boolean open = true;
    protected Thread t;

    public ServerMessaging() {
//        System.out.println("STARTING THREAD");
        t = new Thread(this);
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
                        Message m = MessIN(mess);
                        
                        messQueue.add(m);
                       
                        // add to queue
                        
                        System.out.println("Got message from " + online.get(i).getHandle());
                        
                        switch (m.getType()) {
                        
                        case NEW_MESSAGE :
                        	push(m);
                        	break;
                        case EDIT_MESSAGE :
                        	break;
                        case DELETE_MESSAGE:
                        	break;
                        case CLOSE_CONNECTION:
                        	
                        	break;
                        case LEAVE_SERVER:
                        	online.remove(i);
//                        	push();
                        	break;
                        case UPDATE_SERVER_DATA: 
                        	break;
                       
                        }
//                        if(m.getType() == zero.getType()) {
//                        	push(m);
//                        }
                        
                        
                        
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
    
    public void close() {
    	open = false;
    }

    /**
     * push
     * pushes message to all list persons currently joined
     * @param message
     * @throws IOException
     */
    protected void push(Message message) throws IOException {
    	System.out.println("PUSHING");
        for (int i = 0; i < online.size(); i++) {
            System.out.println("Sending to: " + online.get(i).getHandle());
            online.get(i).getOutput().write(message.toString());
            online.get(i).getOutput().flush();
        }
    }
    
    public boolean closeMess() {
    	open = false;
    	return true;
    }
    
    private Message MessIN(String mess) {
    	String[] unparsedSegments = mess.split(";");
    	
        Message.MessageType type = Message.MessageType.values()[Integer.parseInt(unparsedSegments[0])];
        int id = Integer.parseInt(unparsedSegments[1]);
        Message.ContentType contentType = Message.ContentType.values()[Integer.parseInt(unparsedSegments[2])];
        String sender = unparsedSegments[3];
        String userMessage = unparsedSegments[4];//spliter
        
        Message m = new Message(type, id, contentType, sender, System.currentTimeMillis(), userMessage);
        
        return m;
    }
}