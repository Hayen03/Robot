package hayen.robot.programme.instruction;

import hayen.robot.programme.Programme;

public class Avancer extends Instruction {

	private static final long serialVersionUID = 6462835422815333407L;

	public Avancer(){
		super();
	}
	
	@Override
	public boolean executer(Object... params) {
		Programme p = (Programme)params[0];
		p.getGrille().getRobot().avancer();
		return true;
	}

}
