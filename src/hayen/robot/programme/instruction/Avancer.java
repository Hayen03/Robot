package hayen.robot.programme.instruction;

import hayen.robot.Robot;
import hayen.robot.programme.Bloc;
import hayen.robot.programme.Programme;

public class Avancer extends Instruction {

	private static final long serialVersionUID = 6462835422815333407L;

	public Avancer(){
		super();
	}
	
	@Override
	public boolean executer(Object... params) {
		Bloc b = (Bloc)params[0];
		Programme p = b.getProgramme();
		Robot r = p.getGrille().getRobot();
		r.avancer(p);
		
		return true;
	}

}
