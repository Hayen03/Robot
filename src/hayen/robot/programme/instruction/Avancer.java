package hayen.robot.programme.instruction;

import hayen.robot.Robot;
import hayen.robot.programme.Programme;

public class Avancer extends Instruction {

	private static final long serialVersionUID = 6462835422815333407L;

	public Avancer(){
		super();
	}
	
	// TODO Il va y avoir des choses à changer ici
	@Override
	public boolean executer(Object... params) {
		Programme p = (Programme)params[0];
		Robot r = p.getGrille().getRobot();
		r.avancer(p);
		
		return true;
	}

}
