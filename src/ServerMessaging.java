
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;



public class ServerMessaging implements Runnable {

    //Tracks those online
	ArrayList<Person> online = new ArrayList<Person>();
    
	//Tracks those who "died" LOL
	ArrayList<Ghost> offline = new ArrayList<Ghost>();
    
	//Tracks up to the last 300 hundred messages
    protected CircleQue messQueue = new CircleQue(300);
    
    //Tracks stats.
    public int messagesSent = 0;
    public int messEdited = 0;
    
    //Tracks if this thread is running/ control variable
    private boolean open = true;
    
    //Tracking this thread
    protected Thread t;

    public ServerMessaging() {
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
                        
                        
                       
                        // add to queue
                        
                        System.out.println("Got message from " + online.get(i).getHandle());
                        
                        switch (m.getType()) {
                        
                        case NEW_MESSAGE :
                        	m.setId(messQueue.count);
                        	messQueue.add(m);
                        	messagesSent += 1;
                        	push(m);
                        	break;
                        case EDIT_MESSAGE :
                        	messEdited += 1;
                        	push(m);
                        	m.setType(Message.MessageType.NEW_MESSAGE);
                        	messQueue.set(m.getId(), m);
                        	break;
                        case DELETE_MESSAGE:
                        	break;
                        case CLOSE_CONNECTION:
                        	Message left = new Message(Message.MessageType.NEW_MESSAGE,
									Message.ContentType.TEXT, "Server", 
									("User" +online.get(i).getHandle() +"left\n"));
                        	
                        	Ghost ghost = new Ghost(online.get(i), messQueue.count);
                        	
                        	offline.add(ghost);
                        	online.remove(i);
                        	
                        	push(left);
                        	break;
                        case LEAVE_SERVER:
                        	
                        	break;
                        case UPDATE_SERVER_DATA: 
                        	break;
                       
                        }
                        
                    } else {
                    	//check that person still good
                    	if(online.get(i).getSocket().isClosed() || online.get(i).getSocket().isOutputShutdown()) {
                    		
                    		Message disconnect = new Message(Message.MessageType.NEW_MESSAGE,
									Message.ContentType.TEXT, "Server", 
									("User" +online.get(i).getHandle() +"dissconnected\n"));
                    		
                    		Ghost ghost = new Ghost(online.get(i), messQueue.count);
                    		
                    		offline.add(ghost);
                        	online.remove(i);
                        	push(disconnect);
                    	}
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
    	
    	Message disconnect = new Message(Message.MessageType.CLOSE_CONNECTION,
				Message.ContentType.TEXT, "Server", 
				("I was shut down...\n"));
    	
    	try {
			push(disconnect);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	open = false;
    }

    /**
     * push
     * pushes message to all list persons currently joined
     * @param message
     * @throws IOException
     */
    protected void push(Message message) throws IOException {
        for (int i = 0; i < online.size(); i++) {
            online.get(i).getOutput().write(message.toString());
            online.get(i).getOutput().flush();
        }
    }
    
    /**
     * tinyPush
     * pushes to a single person
     * @param message
     * @param person
     * @throws IOException
     */
    protected void tinyPush(Message message, Person person) throws IOException {
    	person.getOutput().write(message.toString());
    	person.getOutput().flush();
    }
    
    /**
     * closeMess
     * closes messaging thread
     * @return
     */
    public boolean closeMess() {
    	open = false;
    	return true;
    }
    
    /**
     * MessIN
     * takes in message from client then splits into parts, creates new message on server end
     * @param mess String in client message format
     * @return Message
     */
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
    
    protected void messageDecsSERVER(Message m) throws IOException {
    	switch (m.getType()) {
        
        case NEW_MESSAGE :
        	m.setId(messQueue.count);
        	messQueue.add(m);
        	messagesSent += 1;
        	push(m);
        	break;
        case EDIT_MESSAGE :
        	break;
        case DELETE_MESSAGE:
        	break;
        case CLOSE_CONNECTION:
        	
        	break;
        case LEAVE_SERVER:
        	
        	break;
        case UPDATE_SERVER_DATA: 
        	break;
       
        }
    }
    
}