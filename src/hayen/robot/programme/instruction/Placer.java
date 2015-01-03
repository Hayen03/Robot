package hayen.robot.programme.instruction;

import hayen.robot.programme.Programme;
import hayen.robot.programme.Bloc;

public class Placer extends Instruction {
	private static final long serialVersionUID = 6363226372474144372L;
	
	private boolean _placer;
	
	public Placer(boolean placer){
		_placer = placer;
	}
	
	@Override
	public boolean executer(Object... params) {
		Programme p = ((Bloc)params[0]).getProgramme();
		p.getGrille().setCaseActif(_placer);
		return true;
	}

}
