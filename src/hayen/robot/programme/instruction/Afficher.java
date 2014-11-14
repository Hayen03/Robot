package hayen.robot.programme.instruction;

import hayen.robot.programme.Bloc;
import hayen.robot.programme.Compilateur;
import hayen.robot.util.Util;

public class Afficher extends Instruction {
	
	private static final long serialVersionUID = -1447648248726900296L;
	
	private String[] _expression;
	
	public Afficher(String... expression){
		_expression = expression;
	}

	@Override
	public boolean executer(Object... params){
		Bloc p = (Bloc)params[0];
		boolean car = false;
		for (String s : _expression){
			if (s.equals(Compilateur.motCaractere)){ // si un . précède le nombre/variable, on transforme sa valeur en caractère unicode. Ici, on fait seulement dire qu'un point est placé
				car = true;
			}
			else{ // affiche la chose approprié
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

}

