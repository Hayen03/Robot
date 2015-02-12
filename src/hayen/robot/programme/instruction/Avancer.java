package hayen.robot.programme.instruction;

import hayen.robot.Robot;
import hayen.robot.programme.Application;
import hayen.robot.programme.Programme;

public class Avancer extends Instruction {

	public Avancer(){
		super();
	}
	
	// TODO Il va y avoir des choses à changer ici
	@Override
	public void executer(Application app) {
		Programme p = app.getProgramme();
		Robot r = app.getRobot();
		r.avancer(p);
		
	}

}
