package hayen.robot.util;

import java.util.ArrayList;

public class Util {
	
	public static boolean isDigit(String str){
		for (char c : str.toCharArray()) if (!Character.isDigit(c)) return false;
		return true;
	}
	
	public static boolean isAlphaNumeric(String str){
		for (char c : str.toCharArray()) if (!(Character.isDigit(c) || Character.isAlphabetic(c))) return false;
		return true;
	}
	
	public static <T> ArrayList<T> subArray(T[] original, int debut, int fin){
		ArrayList<T> retour = new ArrayList<T>();
		for (int i = 0; i < fin - 1; i++){
			retour.add(original[debut + i]);
		}
		return retour;
	}
	
	public static boolean contains(Object[] array, Object obj){
		int i = 0;
		while (i < array.length){
			if (array[i].equals(obj))
				return true;
			i++;
		}
		return false;
	}
	
	public static String Array2String(Object[] a){
		String s = "[";
		for (Object o : a)
			s += o + ", ";
		return s + "]";
	}
	
}
