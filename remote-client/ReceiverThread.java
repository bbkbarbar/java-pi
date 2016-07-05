
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;


public abstract class ReceiverThread extends Thread {

	//private ObjectInputStream objIn = null;
	private BufferedReader in = null;
	private Client myParent = null;
	private LogManager log = null;
	
	/**
	 * @param in ObjectInputStream from client's socket.
	 * @param log 
	 */
	/*
	public ReceiverThread(ObjectInputStream in, Client parent, LogManager log){
		this.objIn = in;
		this.myParent = parent;
		this.log = log2;
	}/**/
	
	public ReceiverThread(BufferedReader in, Client parent, LogManager log2) {
		this.in = in;
		this.myParent = parent;
		this.log = log2;
	}

	@Override
	public void run() {
		
		Msg msg = null;
		String line = null;
		try {

			//while( (msg = (Msg) objIn.readObject()) != null ){
			while( (line = in.readLine()) != null ){
				msg = Msg.createInstance(line);
				handleMessage(msg);
			}
			
		} catch (IOException e) {
			if(myParent.getWantToDisconnect() == false){
				if(log != null){
					log.w("IOException while try to read message from server..");
				}
			}
		} /*catch (ClassNotFoundException e) {
			if(log != null)
				log.e("Client.Receiver.run() -> ClassNotFoundException");
			e.printStackTrace();
		}/**/
		
	}
	
	/**
	 *  Abstract function to handle incoming Message objects in place of using Receiver object.
	 * @param message
	 */
	protected abstract void handleMessage(Msg message);
	
}
