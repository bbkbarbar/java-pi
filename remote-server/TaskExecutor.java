
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class TaskExecutor {

	public static String readTemp(String param){

		String response = "_PROBLEM_";
		String cmd = "python /home/pi/python/live/getTemp.py -s -1";
		if(Env.runningOnTargetDevice()){
			try {
				Process p = Runtime.getRuntime().exec(cmd);
				BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
				response = in.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			System.out.println("<<RUN PYTHON SCRIPT>>\n" + cmd);
			response = "2\t12.234\t26.027";
		}
		
		return response;
	}

	public static void setColor(int red, int green, int blue) {
		
		//TODO NOW
		
		int redIn16bit   = forceInRange(red   * 16, 0, 4095);
		int greenIn16bit = forceInRange(green * 16, 0, 4095);
		int blueIn16bit  = forceInRange(blue  * 16, 0, 4095);
		
		String cmd = "python /home/pi/python/live/rgb.py"
				+ " -r " + redIn16bit
				+ " -g " + greenIn16bit
				+ " -b " + blueIn16bit;
		
		if(Env.runningOnTargetDevice()){
			try {
				Process p = Runtime.getRuntime().exec(cmd);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			System.out.println("<<RUN PYTHON SCRIPT>>\n" + cmd);
		}
		
	}
	
	private static int forceInRange(int value, int min, int max){
		if(value > max){
			return max;
		}
		if(value < min){
			return min;
		}
		return value;
	}


}
