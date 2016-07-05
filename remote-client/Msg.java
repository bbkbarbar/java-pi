
import java.io.Serializable;

public class Msg implements Serializable{
	
	public static final int versionCode = 100;

	/**
	 * 
	 */
	private static final long serialVersionUID = 4035266283582076042L;


	public static class Types{
		public static final int UNDEFINED = 0,
								COMMAND = 1,
								PLAIN_TEXT = 2, 
								RGB_COMMAND = 3,
								REQUEST = 4;
		
		public static String getTypeText(int type){
			switch (type) {
			case Types.COMMAND:
				return "Command";
			case Types.PLAIN_TEXT:
				return "Plain text";
			case Types.RGB_COMMAND:
				return "RGB command";
			case Types.REQUEST:
				return "Request";

			default:
				return "Undefined";
			}
		}
		
	}

	protected static final String ARGUMENT_SEPARATOR = ", ";

	private int type = Types.UNDEFINED; 

	private String content = null;
	

	
	public Msg(String text){
		this.content = text;
		this.type = Types.PLAIN_TEXT;
	}
	
	public Msg(String text, int type){
		this.content = text;
		this.type = type;
	}

	public static Msg createInstance(String line){
		int resolvedType = Types.UNDEFINED;
		String resolvedContent = "";

		String[] parts = line.split(Msg.ARGUMENT_SEPARATOR);
		for(int i=0; i<parts.lenght; i++){
			String[] data = parts[i].split("=");
			if(data[0].equals("type")){
				resolvedType = data[1];
			}else
			if(data[0].equals("content")){
				resolvedContent = data[1];
			}
		}
		return new Msg(resolvedContent, resolvedType);
	}

	protected String getParameterLine(String item, String value){
		return item + "=" + value;
	}

	public String getInstanceAsLine(){
		return getParameterLine("content", this.content) + Msg.ARGUMENT_SEPARATOR
			 + getParameterLine("type", this.type)
		;
	}
	
	
	public int getType(){
		return this.type;
	}
	
	public String getContent(){
		return this.content;
	}

	protected String getFirstPartOfToString(){
		return "Msg={"
				+ "Type: \"" + Types.getTypeText(this.getType()) + "\",";
	}
	
	protected static String getLastPartOfToString(){
		return "}";
	}
	
	public String toString(boolean needToAppendEndPart){
		String result = this.getFirstPartOfToString();
		if(this.getContent() != null){
			result += " Content: \"" + this.getContent() + "\"";
		}
		if(needToAppendEndPart){
			result += Msg.getLastPartOfToString();
		}
		return result;
	}
	
	@Override
	public String toString(){
		return this.toString(true);
	}
	
}
