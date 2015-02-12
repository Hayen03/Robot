package hayen.robot.programme.instruction;

import hayen.robot.programme.Application;

public class Fin extends Instruction {

	public static final int codeSinon = -2;
	public static final int codeFinCondition = -1;
	
	private int _code;
	
	public Fin(int code){
		_code = code;
	}
	
	@Override
	public void executer(Application app) {
		if (_code == codeSinon){ // avance le programme jusqu'à l'instruction suivant le "Fin" correspondant + dit au programme qu'il est sorti d'un bloc
			Instruction i = null;
			int n = 0;
			while (i == null){
				i = app.getProgramme().getProchaineInstruction();
				if (i.getClass().equals(Fin.class)){
					if (n != 0){
						n--;
						i = null;
					}
				}
				else if (i.getClass().equals(Condition.class)){
					n++;
					i = null;
				}
			}
		}
		else if (_code == codeFinCondition){ // dit au programme qu'il est sorti d'un bloc
			// TODO: dire au programme qu'il est sorti d'un bloc (pas important, ne va peut-être jamais être fait)
		}
		else { // c'est une boucle, on retourne à la ligne indiqué
			// il faudrait aussi dire au programme que l'on est sorti d'un groupe... (si ça devient nécéssaire)
			app.getProgramme().setLigne(_code);
		}
	}

}
