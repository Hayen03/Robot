package hayen.robot;

import java.util.Hashtable;
import java.util.Vector;

import hayen.robot.programme.Application;
import hayen.robot.programme.CaractereInvalideException;
import hayen.robot.programme.Compilateur2;
import hayen.robot.programme.OperationInvalideException;
import hayen.robot.programme.instruction.Instruction;
import hayen.robot.util.Util;
public class Main {

	// FileNotFoundException, ClassNotFoundException, IOException, FichierIncorrectException, OperationInvalideException
	public static void main(String[] args){
		String txt = "var vache boeuf\n"
				+ "vache: 7\n"
				+ "boeuf vache: vache+1\n"
				+ "afficher .97";
		try {
			Instruction[] inst = Compilateur2.compile(txt);
			for (Instruction i : inst)
				System.out.println(i);
			Application app = new Application(inst, 5);
			app.setVisible(true);
			app.run();
		} catch (OperationInvalideException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}

/*
		String txt = "var vache\n"
				+ "vache: 7";
		try {
			Instruction[] inst = Compilateur2.compile(txt);
			for (Instruction i : inst)
				System.out.println(i);
		} catch (OperationInvalideException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/

/*
 String txt = "vache:7+8";
		String[] ms;
		String[] ids;
		Object[] op;
		
		try {
			ms = Compilateur2.separe(txt);
			System.out.println(Util.Array2String(ms));
			
			// trouver les ids
			int i = 0;
			{
				Vector<String> tmp = new Vector<String>();
				while (i < ms.length && !ms[i].equals(":")){
					tmp.add(ms[i]);
					i++;
				}
				ids = tmp.toArray(new String[tmp.size()]);
				i++;
			}
			
			// vérifier l'opération
			{
				String[] tmp = Util.subArray(ms, i, ms.length).toArray(new String[ms.length-i]);
				System.out.println(Util.Array2String(tmp));
				op = Util.string2Operation(tmp);
				System.out.println(Util.Array2String(op));
				boolean b = Compilateur2.verifierOperation(op);
				System.out.println(b);
			}
			
		} catch (CaractereInvalideException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 */
