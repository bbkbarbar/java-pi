
import java.io.IOException;

public class TaskExecutor {

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
