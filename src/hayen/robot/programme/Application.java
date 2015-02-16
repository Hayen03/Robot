package hayen.robot.programme;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import hayen.robot.Robot;
import hayen.robot.graphisme.Console;
import hayen.robot.graphisme.Grille;
import hayen.robot.graphisme.Panel;
import hayen.robot.programme.instruction.Instruction;

public class Application extends JFrame{
	private Programme _programme;
	private Grille _grille;
	private Robot _robot;
	private Console _console;
	
	private Panel _drawingPanel;
	
	public Application(Instruction[] inst, int tailleGrille){
		_programme = new Programme(inst);
		_grille = new Grille(tailleGrille, tailleGrille);
		_robot = _grille.getRobot();
		_console = new Console();
		_programme.setConsole(_console);
		
		_drawingPanel = new Panel();
		
		_drawingPanel.addObject(_grille);
		_drawingPanel.addObject(_robot);
		_drawingPanel.setPreferredSize(_grille.getPreferredSize());
		_drawingPanel.startRefresh(10);
		
		_programme.setApp(this);
		_robot.setAnimer(true);
		
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().add(_drawingPanel);
		getContentPane().add(_console);
		setResizable(false);
		pack();
		setLocation(10, 10);
		setVisible(true);
		_programme.setGrille(_grille);
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
