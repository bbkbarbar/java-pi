
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public abstract class MultiThreadServer extends Thread {

	public static final int DEFAULT_PORT_VALUE = 10713;

	private boolean SERVER_ACCEPT_NEW_CONNECTIONS = true;

	private ServerSocket myServerSocket;

	private HashMap<Integer, ServerThreadForClient> clients = null;
	private int nextClientId = 0;
	
	private int port = DEFAULT_PORT_VALUE;
	
	
	public MultiThreadServer(int port) {
		clients = new HashMap<>();
		nextClientId = 0;
		this.port = port;
	}
	
	
	@Override
	public void run() {
		Socket client = null;
		
		try {
			myServerSocket = new ServerSocket(port);
		} catch (IOException e) {
			showOutput("IOException catched while try initialize ServerSocket(" + port + ")");
		}
		
		System.out.println("Server wait for clients on port: " + port + "..");
		while (SERVER_ACCEPT_NEW_CONNECTIONS) {
			try{
				client = myServerSocket.accept();
				ServerThreadForClient clientThread = new ServerThreadForClient(client, nextClientId, this);
				clients.put(Integer.valueOf(nextClientId), clientThread);
				nextClientId++;
				clientThread.start();
			} catch (IOException e) {
				showOutput("IOException catched while try to accept new connection.");
			}
		}
		
		super.run();
	}
	
	public boolean dropClient(int id){
		
		if(clients == null){
			//TODO: log try to drop client but clients map is NULL.
			return false;
		}
		
		ServerThreadForClient clientToDrop = clients.get(Integer.valueOf(id)); 
		if( clientToDrop == null ){
			//TODO: log try to drop client with invalid id.
			return false;
		}
		
		try{
			clientToDrop.drop();
			clients.remove(Integer.valueOf(id));
			return true;
		}catch(Exception e){
			//TODO: log try to drop client, but something went wrong.
			return false;
		}
		
	}
	
	public boolean sendToClient(Msg msg, int clientId){
		
		if(clients == null){
			//TODO: log try to send message to a client but clients map is NULL.
			return false;
		}
		
		ServerThreadForClient addressee = clients.get(Integer.valueOf(clientId)); 
		if( addressee == null ){
			//TODO: log try to send message to a client with invalid id.
			return false;
		}
		
		return addressee.sendMessage(msg);
		
	}
	
	
	protected void showOutput(String text){}
	
	protected abstract boolean handleInput(Msg msg, int clientId);

	protected abstract void onClientExit(int clientId);


	public String getClientList() {
		String result = "";
		Iterator it = clients.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        result += pair.getKey() + ", ";
	        it.remove(); // avoids a ConcurrentModificationException
	    }
		return result;
	}

}
