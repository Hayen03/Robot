package hayen.robot;

import hayen.robot.programme.Compilateur;
import hayen.robot.programme.Programme;

public class Main {

	public static void main(String[] args) {
		Programme p;
//		for (String s : Compilateur.separe("potato: 3")) System.out.println(s);
		p = new Programme(Compilateur.compile(
				"var i j ",
				"i : 97",
				"si j < 26",
				"	j : 1+",
				"	afficher .i .32",
				"	i : 1+",
				"	goto 3",
				"sinon",
				"	afficher .10 .102.105.110",
				"fin"
				));
		p.executer();

		
	}

}
