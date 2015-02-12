package hayen.robot.programme;

import hayen.robot.Robot;
import hayen.robot.graphisme.Console;
import hayen.robot.graphisme.Grille;
import hayen.robot.programme.instruction.Instruction;

public class Application {
	private Programme _programme;
	private Grille _grille;
	private Robot _robot;
	private Console _console;
	
	public Application(Instruction[] inst, int tailleGrille){
		_programme = new Programme(inst);
		_grille = new Grille(tailleGrille, tailleGrille);
		_robot = _grille.getRobot();
		_console = new Console();
		_programme.setConsole(_console);
	}
	
	public Grille getGrille(){
		return _grille;
	}
	public Robot getRobot(){
		return _robot;
	}
	public Programme getProgramme(){
		return _programme;
	}
	
}
