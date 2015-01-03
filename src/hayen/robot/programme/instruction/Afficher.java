package hayen.robot.programme.instruction;

import hayen.robot.graphisme.Console;
import hayen.robot.programme.Bloc;
import hayen.robot.programme.Compilateur;
import hayen.robot.programme.Programme;
import hayen.robot.util.Util;

public class Afficher extends Instruction {
	
	private static final long serialVersionUID = -1447648248726900296L;
	
	private String[] _expression;
	
	public Afficher(String... expression){
		_expression = expression;
	}

	@Override
	public boolean executer(Object... params){
		Bloc b = (Bloc)params[0];
		Programme p = b.getProgramme();
		boolean car = false;
		String buffer = "";
		int v;
		
		for (String s : _expression){
			if (s.equals(Compilateur.motCaractere)){ // si un . pr�c�de le nombre/variable, on transforme sa valeur en caract�re unicode. Ici, on fait seulement dire qu'un point est plac�
				car = true;
			}
			else{ // affiche la chose appropri�

				if (Util.isDigit(s)) 
					v = Integer.parseInt(s);
				else 
					v = b.getVariable(s);
				
				if (car) 
					buffer += (char)v;
				else
					buffer += v;
				car = false;
			}
		}
		
		if (p == null)
			System.out.print(buffer);
		else {
			Console c = p.getConsole();
			if (c == null)
				System.out.print(buffer);
			else
				c.print(buffer);
		}
		
		return true;
	}
	
	@Override
	public String toString(){
		String s = "Print: ";
		for (String e : _expression) s += e + ", ";
		return s; 
	}

}

