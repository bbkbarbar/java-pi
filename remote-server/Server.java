import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public abstract class Server {

	public static final boolean TESTINGMODE = false;

	public static final int PORT_IN_DEFAULT_VALUE = 10713;

	private boolean SERVER_ACCEPT_NEW_CONNECTIONS = true;

	private ServerSocket myServerSocket;

	private int port;
	private int maxConnections = 0; // TODO: megcsinalni, hogy konstruktorbol
									// lehessen beallitani, es lehessen default
									// ertekkel is inicializalni

	private ArrayList<ServingClientThread> openThreads;

	/**
	 * Constructor of Server class.
	 * 
	 * @param portIn
	 */
	public Server(int portIn) {
		super();
		this.port = portIn;
		init(port);
	}

	/**
	 * Constructor of Server class.
	 * <p>
	 * Init with default input port value.
	 */
	public Server() {
		super();
		init(PORT_IN_DEFAULT_VALUE);
	}

	protected void init(int inputPort) {
		openThreads = new ArrayList<>();
		this.port = inputPort;
	}

	public void start() {
		// Contains the number of connections (and represents the idx of the
		// next connection).

		System.out.println("Wait for clients @ port: " + this.getPort() + "...");

		try {

			myServerSocket = new ServerSocket(port);
			Socket server;

			while ((openThreads.size() < maxConnections) || (maxConnections == 0) && SERVER_ACCEPT_NEW_CONNECTIONS) {
				server = myServerSocket.accept();
				ServingClientThread servingThread = new ServingClientThread(server, openThreads.size(), this) {

					@Override
					public boolean handleMsg(String receivedLine, int idxOfThread) {
						return handleRecievedMessage(receivedLine, idxOfThread);
					}

					@Override
					public void handleExceptionIOWhenServerGetMessage(IOException ioExc) {
						// TODO Auto-generated method stub
						
					}

				};
				openThreads.add(servingThread);
				servingThread.start();
			}

			waitUntilClientConnectionClosed();

			myServerSocket.close();

		} catch (IOException ioe) {
			handleExceptionWhileServerWaitNewConnecton(ioe);
		}
	}

	private void waitUntilClientConnectionClosed() {
		// TODO
	}

	protected void removeClientThread(int idxOfServerThread) {
		for (int i = 0; i < openThreads.size(); i++) {
			if (((ServingClientThread) openThreads.get(i)).getIdx() == idxOfServerThread) {
				openThreads.remove(i);
			}
		}
	}

	public int getPort() {
		return this.port;
	}

	public abstract boolean handleRecievedMessage(String message, int idxOfThread);

	public void handleExceptionWhileServerWaitNewConnecton(IOException exception){}

	public void handleExceptionIOWhenServerThreadGetMessage(IOException iofExc){}

	public void sendResponse(String response, int idOfThread) {
		openThreads.get(idOfThread).sendResponse(response);
	}

}
