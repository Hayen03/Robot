package hayen.robot;

import java.util.Hashtable;

import hayen.robot.programme.Application;
import hayen.robot.programme.CaractereInvalideException;
import hayen.robot.programme.Compilateur2;
import hayen.robot.programme.instruction.Instruction;
import hayen.robot.util.Util;
public class Main {

	// FileNotFoundException, ClassNotFoundException, IOException, FichierIncorrectException, OperationInvalideException
	public static void main(String[] args){
		Application app = new Application(new Instruction[0], 5);
		app.setVisible(true);
	}
	
}
