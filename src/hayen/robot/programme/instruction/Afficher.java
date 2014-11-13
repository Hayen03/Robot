package hayen.robot.programme.instruction;

import java.io.DataOutputStream;
import java.io.IOException;

import hayen.robot.programme.Compilateur;
import hayen.robot.programme.Programme;
import hayen.robot.util.Util;

public class Afficher extends Instruction {
	
	private String[] _expression;
	
	public Afficher(String... expression){
		_expression = expression;
	}

	@Override
	public boolean run(Programme p) {
		boolean car = false;
		for (String s : _expression){
			if (s.equals(Compilateur.motCaractere)){
				car = true;
			}
			else{
				int v;
				if (Util.isDigit(s)) v = Integer.parseInt(s);
				else v = p.getVariable(s);
				
				if (car) System.out.print((char)v);
				else System.out.print(v);
				car = false;
			}
		}
		return true;
	}
	
	@Override
	public String toString(){
		String s = "Print: ";
		for (String e : _expression) s += e + ", ";
		return s; 
	}

/*	@Override
	public void enregistrer(DataOutputStream fichier) throws IOException {
			fichier.writeByte(Instruction.type.afficher.numero);
			for (String str : _expression){
				fichier.writeChar('$');
				fichier.writeUTF(str);
			}
			fichier.writeChar('&');;
	}
*/
}

