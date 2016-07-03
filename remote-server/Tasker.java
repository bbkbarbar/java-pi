
import java.text.SimpleDateFormat;
import java.util.Date;


public class Tasker {
	
	public static final boolean DEBUG_MODE = true;

	protected static Tasker me = null;
	
	private MultiThreadServer myServer = null;
	
	public static void main(String[] args) {
		
		me = new Tasker();
		me.start();
		
	}
	
	private void start(){
		System.out.println("Start server..");
		myServer = new MultiThreadServer(){
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
			if( msg.getContent().startsWith("dateTime")){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
				Date now = new Date();
				String dateStr = sdf.format(now);
				myServer.sendToClient(new Msg(dateStr, Msg.Types.PLAIN_TEXT), clientId);
			}
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
