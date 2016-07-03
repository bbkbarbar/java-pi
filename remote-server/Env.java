
import java.io.File;
import java.util.Map;

public class Env {
	
	public static boolean DEBUG_NEEDED = true;
	
	
	/**
	 * Name of data folder to store files of tasker application
	 * <br> Default name: <b>"taskerData"</b>
	 */
	public static final String NAME_OF_DATA_FOLDER = "taskerData";
	
	
	
	/**
	 * @return true if app running on target device (with linux OS)
	 */
	public static boolean runningOnTargetDevice(){
		return (System.getProperty("os.name").contains("Linux"));
	}
	
	/**
	 * @return path separator ("/" or "\") according to the used operating system.
	 */
	public static String getPathSeparator(){
		return (runningOnTargetDevice()?("/"):("\\"));
	}
	
	private static String getUserHomeDir(){
		return System.getProperty("user.home");
	}
	
	/**
	 * @return path of data folder (named as value of NAME_OF_DATA_FOLDER) 
	 */
	public static String getDataFolderPath(){
		return getUserHomeDir() + getPathSeparator() + NAME_OF_DATA_FOLDER + getPathSeparator();
	}
	
	/**
	 *  Check that the data folder with defined name (NAME_OF_DATA_FOLDER) exists or
	 *  <br> create folder if not exists.
	 */
	public static void guaranteeDataFolderExists(){
		File f = new File(getDataFolderPath());
		if(f.exists() && f.isDirectory()){
			if(DEBUG_NEEDED)
				System.out.println("Directory exists");
		}else{
			if(DEBUG_NEEDED)
				System.out.println("Directory NOT exists");
			f.mkdirs();
			if(DEBUG_NEEDED){
				if(f.exists() && f.isDirectory()){
					System.out.println("Directory created");
				}else{
					System.out.println("Directory CAN NOT BE CREATED");
				}
			}
		}
	}
	
	
	public static void d(){
		
		Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            System.out.format("%s=%s%n",
                              envName,
                              env.get(envName));
        }
		
	}
	
}
