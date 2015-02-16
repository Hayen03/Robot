package hayen.robot;

import java.util.Hashtable;

import hayen.robot.programme.CaractereInvalideException;
import hayen.robot.programme.Compilateur2;
import hayen.robot.util.Util;
public class Main {

	// FileNotFoundException, ClassNotFoundException, IOException, FichierIncorrectException, OperationInvalideException
	public static void main(String[] args){
		String op = "48 +( 5 / -3)";
		Object[] op2 = Util.string2Operation(op);
		
		System.out.println(Util.Array2String(op2));
		
		if (Compilateur2.verifierOperation(op2))
			System.out.println("vrai");
		else
			System.out.println("faux");
		
	}
	
}
