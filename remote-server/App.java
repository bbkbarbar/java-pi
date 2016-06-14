import java.io.IOException;

public class App {

	private static final boolean DEBUG_MODE = false;
	
	public static final int PORT = (DEBUG_MODE ? 13003 : 10713);

	private static App me = null;
	
	private Server myServer = null;
	
	private Responser myResponser = null;
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		me = new App();
	}
	
	public App(){
		
		initServer();
		
	}
	
	private void initServer(){
		
		myResponser = new Responser();
		myServer = new Server(PORT){
			@Override
			public boolean handleRecievedMessage(String msg, int idOfThread) {
				System.out.println("ServerApp.handleRecievedMessage: |" + msg + "|, " + idOfThread);
				processIncomintMessage(msg, idOfThread);
				return true;
			}
		};
		myServer.start();
		
	}
	
	private void runScript(String script){
		try {
			Process p = Runtime.getRuntime().exec(script);
		} catch (IOException e) {
			System.out.println("Exception catched while try to run script.");
		}
	}
	
	private void processIncomintMessage(String message, int idOfThread){
		
		if(message.startsWith("c:")){
			
			if(message.startsWith("c:LEDS_ON")){
				runScript("python /home/pi/python/io.py -p 3 -s 1");
			}
			
		}
		
		myServer.sendResponse(
			myResponser.getResponse(message),
			idOfThread
		);
		
	}

}
