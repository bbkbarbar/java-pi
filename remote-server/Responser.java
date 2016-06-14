
import java.text.SimpleDateFormat;
import java.util.Date;

public class Responser {

	private static final String DEFAULT_RESPONSE_FOR_UNHANDELD_REQUESTS = "REQUEST IS UNHANDLED"; 
	
	public String getResponse(String message) {
		
		if(message.startsWith("getDate")){
			return "Time: " + System.currentTimeMillis();
		}else
			
		if(message.startsWith("dateTime")){
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
			
			Date now = new Date();
			String dateStr = sdf.format(now);
			
			return "Res: " + dateStr;
		}else
			
			return this.DEFAULT_RESPONSE_FOR_UNHANDELD_REQUESTS;
		
	}

}
