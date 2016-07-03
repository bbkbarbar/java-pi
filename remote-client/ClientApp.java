
public class ClientApp {

	private static ClientApp me = null;

	private static final boolean DEBUG_MODE = false;

	public static String ver = "0.1";
	public static final String DEFAULT_SERVER_HOSTNAME = (DEBUG_MODE ? "localhost" : "192.168.0.101");
	public static final int DEFAULT_SERVER_PORT = (DEBUG_MODE ? 13003 : 10713);

	public String SERVER_HOSTNAME = DEFAULT_SERVER_HOSTNAME;
	public int SERVER_PORT = -1;

	private LogManager log = null;
	private Client myClient = null;
	
	//TODO
	boolean responseReceived = false;
	
	public static void main(String[] args) {

		if (DEBUG_MODE) {
			ver = ver + " DEBUG MODE";
		}

		me = new ClientApp();
		me.start(args);
	}

	private void start(String[] args) {
	
		this.log = new LogManager("Client", LogManager.Level.INFO){

			@Override
			public void showInfo(String text) {
				System.out.println(text);
			}

			@Override
			public void showWarn(String text) {
				System.out.println(text);
			}

			@Override
			public void showError(String text) {
				System.out.println(text);
			}
			
		};
		
		int portArg = DEFAULT_SERVER_PORT;
		if(args.length>1){
			try{
				portArg = Integer.valueOf(args[1].trim());
			}catch(Exception e){
				portArg = DEFAULT_SERVER_PORT;
			}
		}
		
		myClient = new Client(DEFAULT_SERVER_HOSTNAME, portArg) {
			
			@Override
			protected void showOutput(String text) {
				System.out.println(text);
			}
			
			@Override
			protected void handleRecievedMessage(Msg message) {
				System.out.println("Message received from SERVER: " + message.getContent());
				System.out.println(message.toString());
			}
			
			@Override
			public void onConnectionRefused(String host, int port) {
				System.out.println("Connection refused (" + host + " @ " + port + ").");
			}
			
		};
		myClient.setLogManager(log);
		myClient.start();
		
		System.out.println("Clint:: clientThread started");
		
		if(myClient.waitWhileIsInitialized()){
			System.out.println("CLIENT IS INITILAIZED");
		}
		// wait for answer..
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {} /**/
		
		if(myClient.sendMessage(new Msg("dateTime", Msg.Types.COMMAND))){
			System.out.println("Sent: " + "dateTime");
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
		
		if(myClient.sendMessage(new RGBMessage("setAll", 255,127,10))){
			System.out.println("Sent: " + "Color");
		}
		
		
		myClient.disconnect();

	}
	
	public void showOutput(String text){
		System.out.println(text);
	}

}
