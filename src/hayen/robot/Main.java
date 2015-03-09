package hayen.robot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import hayen.robot.programme.Application;
import hayen.robot.programme.OperationInvalideException;

public class Main {

	public static void main(String[] args){
		try {
			Application app = new Application(new File("source/exempleRobot2.pr"), 10);
//			if (JOptionPane.showConfirmDialog(null, "ok") == JOptionPane.OK_OPTION)
				app.start();
		} 
		catch (OperationInvalideException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
