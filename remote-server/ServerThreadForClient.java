
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;


public class ServerThreadForClient extends Thread {

	public static final int ID_UNDEFINED = -1;
	
	private MultiThreadServer myServer = null;
	private int myId = ServerThreadForClient.ID_UNDEFINED;

	private Socket client = null;
	private BufferedReader in = null;
	//private ObjectInputStream objIn = null;
	private PrintWriter out = null;
	//private ObjectOutputStream objOut = null;
	
	
	public ServerThreadForClient(Socket client, int id, MultiThreadServer server) {
		this.myServer = server;
		this.client = client;
		this.myId = id;
	}
	
	@Override
	public void run() {
		
		try {
			//Channel channel = new Channel( client.getInputStream() );

			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			//objIn = new ObjectInputStream(client.getInputStream());
			//TODO HERE
			//objIn = new ObjectInputStream(Channels.newInputStream(channel));
			out = new PrintWriter(new PrintStream(client.getOutputStream()), true);
			//objOut = new ObjectOutputStream(client.getOutputStream());
			
			//if(objIn == null){
			if(in == null){
				myServer.showOutput("Client (" + myId + "): IN is NULL.");
			}
			
			String line = null;
			Msg receivedMsg = null;
			try{
				//while ((receivedMsg = (Msg) objIn.readObject()) != null){
				while ( (line = in.readLine()) != null){
					receivedMsg = Msg.createInstance(line);
					if(receivedMsg.getType()==Msg.Types.COMMAND && receivedMsg.getContent().equals(Commands.CLIENT_EXIT)){
						myServer.onClientExit(myId);
					}else{
						myServer.handleInput(receivedMsg, myId);
					}
				}
			}catch(NullPointerException npe){
				myServer.showOutput("Client (" + myId + "): NullPointerException cought");
			}/*catch(ClassNotFoundException cnfe){
				myServer.showOutput("ClassNotFoundException cought");
			}/**/

		} catch (IOException ioExc) {
			//TODO: handleExceptionIOWhenServerGetMessage(ioExc);
		}
		
		super.run();
	}

	@SuppressWarnings("deprecation")
	public void drop() {
		try{
			//objIn.close();
			in.close();
		}catch(Exception e){}
		try{
			//objOut.close();
			out.close();
		}catch(Exception e){}
		try{
			client.shutdownInput();
			client.close();
		}catch(Exception e){
			//TODO:
		}finally {
			try{
				ServerThreadForClient.this.interrupt();
			}catch(Exception e){}
			try{
				ServerThreadForClient.this.stop();
			}catch(Exception e){}
		}
		//TODO: client instance dropped (myId).
	}

	public boolean sendMessage(Msg message) {
		if(Tasker.DEBUG_MODE)
			System.out.println("Send to client: [" + myId + "]: " + message.getContent());
		//if(objOut == null)
		if(out == null)
			return false;
		
		String line = message.getInstanceAsLine();
		try{
			//objOut.writeObject(message);
			//objOut.flush();
			out.println(line);
			out.flush();
		}catch(Exception e){
			//TODO log exception 
			return false;
		}
		return true;
	}

	
	
}
