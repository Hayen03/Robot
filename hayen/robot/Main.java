package hayen.robot;

import hayen.robot.programme.Compilateur;
import hayen.robot.programme.Programme;

public class Main {

	public static void main(String[] args) {
		Programme p;
//		for (String s : Compilateur.separe("potato: 3")) System.out.println(s);
		p = new Programme(Compilateur.compile(
				"var i L",
				"i : 1",
				"L:97 # une lettre minuscule",
				"si i < 27",
				"	afficher .L",
				"	i L : 1+",
				"	goto 4",
				"fin"
				));
		p.executer();

		
	}

}
