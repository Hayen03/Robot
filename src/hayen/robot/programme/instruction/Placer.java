package hayen.robot.programme.instruction;

import hayen.robot.programme.Programme;

public class Placer extends Instruction {
	
	private boolean _placer;
	
	public Placer(boolean placer){
		_placer = placer;
	}
	
	// TODO: réparer lorsque je vais le briser
	@Override
	public boolean executer(Object... params) {
		Programme p = (Programme)params[0];
		p.getGrille().setCaseActif(_placer);
		return true;
	}

}
