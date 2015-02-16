package hayen.robot;

import java.util.Hashtable;

import hayen.robot.programme.CaractereInvalideException;
import hayen.robot.programme.Compilateur2;
import hayen.robot.util.Util;
public class Main {

	// FileNotFoundException, ClassNotFoundException, IOException, FichierIncorrectException, OperationInvalideException
	public static void main(String[] args){
		String op = "48 +( (5+2) / -3)";
		Object[] op2 = Util.string2Operation(op);
		
		if (Compilateur2.verifierOperation(op2)){
			System.out.println("vrai");
			int n = Util.evaluer(op2, new Hashtable<String, Integer>());
			System.out.println(n);
		}
		else
			System.out.println("faux");
		
	}
	
}
