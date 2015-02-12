package hayen.robot;

import java.util.Hashtable;

import hayen.robot.util.Util;
public class Main {

	// FileNotFoundException, ClassNotFoundException, IOException, FichierIncorrectException, OperationInvalideException
	public static void main(String[] args){
		
		String s = "(1+vachon)*-(4-(5/3))";
		Object[] o = Util.string2Operation(s);
		for (Object obj : o)
			System.out.print(obj + " ");
		System.out.println(" = ");
		Hashtable<String, Integer> v = new Hashtable<String, Integer>();
		v.put("vachon", 4);
		System.out.println(Util.evaluer(o, v));
	}
	
}
