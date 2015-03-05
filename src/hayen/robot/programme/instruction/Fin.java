package hayen.robot.programme.instruction;

import hayen.robot.programme.Application;
import hayen.robot.programme.Programme;

public class Fin extends Instruction {

	public static final int codeSinon = -2;
	public static final int codeFinCondition = -1;
	
	private int _code;
	
	public Fin(int code){
		_code = code;
	}
	
	@Override
	public void executer(Application app) {
		Programme p = app.getProgramme();
		if (_code == codeSinon){ // avance le programme jusqu'à l'instruction suivant le "Fin" correspondant + dit au programme qu'il est sorti d'un bloc
			Instruction i = null;
			int ln = p.getLigne();
			int n = 0;
			int deltaLn = 1;
			while (ln+deltaLn < p.longueur()){
				i = p.getInstructionA(ln + deltaLn);
				if (i instanceof Condition){
					n++;
				}
				else if (i instanceof Fin){
					if (n == 0)
						break;
					else
						n--;
				}
				deltaLn++;
			}
			p.setLigne(ln + deltaLn);
		}
		else if (_code == codeFinCondition){ // dit au programme qu'il est sorti d'un bloc
		}
		else { // c'est une boucle, on retourne à la ligne indiqué
			// il faudrait aussi dire au programme que l'on est sorti d'un groupe... (si ça devient nécéssaire)
			app.getProgramme().setLigne(_code-1);
		}
		app.getProgramme().decremente();
	}

}
