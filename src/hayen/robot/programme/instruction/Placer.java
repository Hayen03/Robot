package hayen.robot.programme.instruction;

import hayen.robot.Robot;
import hayen.robot.programme.Application;

public class Placer extends Instruction {
	
	private boolean _placer;
	
	public Placer(boolean placer){
		_placer = placer;
	}
	
	// TODO: réparer lorsque je vais le briser
	@Override
	public void executer(Application app) {
		Robot r = app.getRobot();
		app.getGrille().setCaseActif(r.getPosition(), _placer);
		app.getPanel().repaint();
	}

}
