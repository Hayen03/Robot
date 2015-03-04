package hayen.robot.programme.instruction;

import hayen.robot.programme.Application;

public class Avancer extends Instruction {

	public Avancer(){
		super();
	}
	
	// TODO Il va y avoir des choses à changer ici
	@Override
	public void executer(Application app) {
		app.getRobot().avancer();
		
	}

}
