
import java.text.SimpleDateFormat;
import java.util.Date;


public class Tasker {
	
	public static final boolean DEBUG_MODE = true;

	public static final int DEFAULT_PORT = 10713;

	protected static Tasker me = null;
	
	private MultiThreadServer myServer = null;
	
	public static void main(String[] args) {
		
		System.out.println("args.length: " + args.length);
		int portArg = DEFAULT_PORT;
		if(args.length>1){
			try{
				portArg = Integer.valueOf(args[1].trim());
			}catch(Exception e){
				portArg = DEFAULT_PORT;
			}
		}
		
		me = new Tasker();
		me.start(portArg);
		
	}
	
	private void start(int port){
		System.out.println("Start server..");
		myServer = new MultiThreadServer(port){
			@Override
			protected boolean handleInput(Msg msg, int clientId) {
				return Tasker.me.processCommand(msg, clientId);
			}

			@Override
			protected void onClientExit(int clientId) {
				System.out.println("Client disconnected [" + clientId + "]");
			}
		};
		myServer.start();
		System.out.println("Server started.");
	}
	
	protected boolean processCommand(Msg msg, int clientId){

		if(msg.getType() == Msg.Types.COMMAND){
			if( msg.getContent().startsWith(Commands.GET_DATE)){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
				Date now = new Date();
				String dateStr = sdf.format(now);
				myServer.sendToClient(new Msg(dateStr, Msg.Types.PLAIN_TEXT), clientId);
			}
			else
			if( msg.getContent().startsWith(Commands.GET_TEMP)){
				String response = TaskExecutor.readTemp("not_used_yet");
				myServer.sendToClient(new Msg("Temp: " + response, Msg.Types.PLAIN_TEXT), clientId);
			}
			/*
			else
			if( msg.getContent().contains("getClientList")){
				System.out.println("Client wants to get current client list.");
				String response = myServer.getClientList();
				myServer.sendToClient(new Msg("Clients: " + response, Msg.Types.PLAIN_TEXT), clientId);
				System.out.println("ClientList sent to client:\n" + response);
			}/**/
		}else

		if(msg.getType() == Msg.Types.RGB_COMMAND){
			RGBMessage rgbMsg = (RGBMessage)msg;
			System.out.println("Color receied from client [" + clientId + "]: " + rgbMsg.toString());
			TaskExecutor.setColor(rgbMsg.getRed(), rgbMsg.getGreen(), rgbMsg.getBlue());
			myServer.sendToClient(new Msg("New color accepted.", Msg.Types.PLAIN_TEXT), clientId);
		}else
		{
			System.out.println("Message receied from client [" + clientId + "]: " + msg.toString());
		}
		
		return false;
	}

}
