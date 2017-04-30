package explorer.utils;

public class XORDataException extends Exception{
	String message;
	public XORDataException(String string){
		message = string + "\t does not have the same key length";
	}
	public String getMessage(){
		return message;
	}
}