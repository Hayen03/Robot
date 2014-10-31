package hayen.robot.util;

public class Util {
	
	public static boolean isDigit(String str){
		for (char c : str.toCharArray()) if (!Character.isDigit(c)) return false;
		return true;
	}
	
	public static boolean isAlphaNumeric(String str){
		for (char c : str.toCharArray()) if (!(Character.isDigit(c) || Character.isAlphabetic(c))) return false;
		return true;
	}
	
}
