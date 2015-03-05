package hayen.robot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;

import hayen.robot.programme.Application;
import hayen.robot.programme.Compilateur2;
import hayen.robot.programme.OperationInvalideException;

public class Main {

	public static void main(String[] args){
		String inst = read("source/exempleRobot2.pr");
		if (inst == null)
			return;
		try {
			Application app = new Application(Compilateur2.compile(inst), 10);
//			if (JOptionPane.showConfirmDialog(null, "ok") == JOptionPane.OK_OPTION)
				app.start();
		} 
		catch (OperationInvalideException e) {
			e.printStackTrace();
		}
		
	}
	public static String read(String path){
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(path));
			StringBuilder sb = new StringBuilder();
			String ln = br.readLine();
			while (ln != null){
				sb.append(ln + '\n');
				ln = br.readLine();
			}
			
			br.close();
			return sb.toString();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
