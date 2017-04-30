package explorer.utils;

public class StringNotHexException extends Exception{
	String message;
	public StringNotHexException(String string){
		message = string + "\tis not an Hexadecimal Character";
	}
	public String getMessage(){
		return message;
	}
}