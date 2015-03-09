package hayen.robot.programme.instruction;

import hayen.robot.graphisme.Console;
import hayen.robot.programme.Application;
import hayen.robot.programme.Compilateur2;
import hayen.robot.programme.Programme;
import hayen.robot.util.Util;

public class Afficher extends Instruction {

	private String[] _expression;

	public Afficher(String... expression){
		_expression = expression;
	}

	@Override
	public void executer(Application app){
		Programme p = app.getProgramme();
		boolean car = false;
		String buffer = "";
		int v;

		for (String s : _expression){
			if (s.equals(Compilateur2.motCaractere)){ // si un . pr�c�de le nombre/variable, on transforme sa valeur en caract�re unicode. Ici, on fait seulement dire qu'un point est plac�
				car = true;
			}
			else{ // affiche la chose appropri�

				if (Util.isDigit(s)) 
					v = Integer.parseInt(s);
				else 
					v = p.getVariable(s);

				if (car) 
					buffer += (char)v;
				else
					buffer += v;
				car = false;
			}
		}

		Console c = p.getApp().getConsole();
		if (c == null)
			System.out.print(buffer);
		else
			c.print(buffer);

	}

	@Override
	public String toString(){
		String s = "Print: ";
		for (String e : _expression) s += e + ", ";
		return s; 
	}

}

