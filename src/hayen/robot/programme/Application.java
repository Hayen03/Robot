package hayen.robot.programme;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import hayen.robot.Direction;
import hayen.robot.Robot;
import hayen.robot.graphisme.Console;
import hayen.robot.graphisme.Grille;
import hayen.robot.graphisme.DrawingPanel;
import hayen.robot.programme.instruction.Instruction;

public class Application extends Thread{
	private Programme _programme;
	private Grille _grille;
	private Robot _robot;
	private Console _console;
	
	private DrawingPanel _drawingPanel;
	private JFrame _fenetrePrincipale;
	
	private boolean _enPause = false;
	
	public Application(Instruction[] inst, int tailleGrille){
		_programme = new Programme(inst);
		_grille = new Grille(tailleGrille, tailleGrille);
		_console = new Console();
		_programme.setConsole(_console);
		_robot = new Robot(this);
		
		_drawingPanel = new DrawingPanel();
		_fenetrePrincipale = new JFrame();
		
		_drawingPanel.addObject(_grille);
		_drawingPanel.addObject(_robot);
		_drawingPanel.setPreferredSize(_grille.getPreferredSize());
		
		_programme.setApp(this);
		_grille.setApp(this);
		_robot.setAnimer(true);
		_programme.setConsole(_console);
		
		_fenetrePrincipale.setLayout(new BoxLayout(_fenetrePrincipale.getContentPane(), BoxLayout.Y_AXIS));
		_fenetrePrincipale.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_fenetrePrincipale.getContentPane().add(_drawingPanel);
		_fenetrePrincipale.getContentPane().add(_console);
		_fenetrePrincipale.setResizable(false);
		_fenetrePrincipale.pack();
		_fenetrePrincipale.setLocation(10, 10);
		_fenetrePrincipale.setVisible(true);
		
		_programme.assigner("largeur", (int)_grille.getTailleGrille().getX());
		_programme.assigner("hauteur", (int)_grille.getTailleGrille().getY());
		_programme.assigner("posx", 0);
		_programme.assigner("posy", 0);
		_programme.assigner("orientation", _robot.getOrientation());
		_programme.assigner("gauche", 1);
		_programme.assigner("droite", -1);
		_programme.assigner("nord", Direction.Nord);
		_programme.assigner("ouest", Direction.Ouest);
		_programme.assigner("sud", Direction.Sud);
		_programme.assigner("est", Direction.Est);
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
	public DrawingPanel getPanel(){
		return _drawingPanel;
	}
	public Console getConsole(){
		return _console;
	}
	public JFrame getFenetrePrincipale(){
		return _fenetrePrincipale;
	}
	
	public void run(){
		_programme.incremente();
		Instruction i = _programme.getProchaineInstruction();
		while (i != null){
			if (!_enPause){
				i.executer(this);
				i = _programme.getProchaineInstruction();
			} 
			else{
				try {
					sleep(10);
				} catch (InterruptedException e) {}
			}
		}
	}
	
	public synchronized void setEnPause(boolean p){
		_enPause = p;
	}
	public synchronized boolean getEnPause(){
		return _enPause;
	}
	
}
