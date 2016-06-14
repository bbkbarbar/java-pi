import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class ServingClientThread extends Thread {

	private int idxOfServerThread = -1;
	private Server myParent = null;
	
	private BufferedReader in = null;
	private PrintWriter out = null;
	
	private Socket mySocket = null;

	public ServingClientThread(Socket socket, int myIdx, Server myParent) {

		super();
		this.mySocket = socket;
		this.idxOfServerThread = myIdx;
		this.myParent = myParent;

	}

	@Override
	public void run() {

		try {

			in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
			out = new PrintWriter(new PrintStream(mySocket.getOutputStream()), true);
			String receivedLine = in.readLine();
			if(in == null){
				System.out.println("In is NULL");
			}
			if(receivedLine == null){
				System.out.println("receivedLine is NULL");
			}
			try{
				while (!receivedLine.equals("--exit") ){
					if (handleMsg(receivedLine, idxOfServerThread) == false) {
						//TODO: unhandled message
					}
	
					receivedLine = in.readLine();
				}
			}catch(NullPointerException npe){
				System.out.println("NullPointerException cought");
			}

			mySocket.close();
			myParent.removeClientThread(idxOfServerThread);

		} catch (IOException ioExc) {
			handleExceptionIOWhenServerGetMessage(ioExc);
		}

	}

	public int getIdx() {
		return this.idxOfServerThread;
	}

	public abstract boolean handleMsg(String receivedLine, int idxOfServerThread2);

	//public abstract void handleExceptionClassNotFoundWhenServerGetMessage(ClassNotFoundException cnfExc);

	public abstract void handleExceptionIOWhenServerGetMessage(IOException ioExc);
	
	public void sendResponse(String response){
		/*
		int numberOfAttemps = 0;
		while(out != null && numberOfAttemps < 5){
			numberOfAttemps++;
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
		}/* INTERRUPT */
		if(out != null){
			out.println(response);
			out.flush();
		}else{
			System.out.println("ServingClientThread.out is null");
		}
	}

}
