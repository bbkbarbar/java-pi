
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

public class SenderThread extends Thread {

	//private ObjectOutputStream objOut = null;
	private PrintWriter out = null;
	
	private LogManager log = null;
	
	/*
	public SenderThread(ObjectOutputStream aOut, LogManager l){
		this.objOut = aOut;
		this.log = l;
	}/**/
	
	public SenderThread(PrintWriter out, LogManager l) {
		this.out = out;
		this.log = l;
	}

	public void sendMsg(Msg msg) {
		
		try {
			//objOut.writeObject( msg );
			out.println(msg.getInstanceAsLine());
			return;
			
		} catch (Exception e) {
			if(log != null)
				log.e("Client.SenderThread.sendMsg() -> IOException catched.");
			e.printStackTrace();
		}
		
	}
	
	
}
