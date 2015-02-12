package hayen.robot.programme;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

import hayen.robot.Direction;
import hayen.robot.Robot;
import hayen.robot.graphisme.Console;
import hayen.robot.graphisme.Grille;
import hayen.robot.programme.instruction.*;

public class Programme {
	
	private Application _app;
	private Console _console;
	
	protected final Instruction[] _instructions;
	protected Hashtable<String, Integer> _variables;
	protected int _ligne;
	protected final int _longueur;
	
	public Programme(Instruction[] instructions){
		
		_instructions = instructions;
		_variables = new Hashtable<String, Integer>();
		_ligne = 0;
		_longueur = _instructions.length;
		
		_console = null;
	}
	public Programme(String adresse) throws FileNotFoundException, IOException, ClassNotFoundException, FichierIncorrectException, OperationInvalideException{
		_instructions = getInstructionsFromFichier(adresse);
		_longueur = _instructions.length;
		_console = null;
	}
	
	public Programme setGrille(Grille g){ // TODO Il y aura surement des modifications à apporter ici
		Robot r = g.getRobot();
		assigner("largeur", (int)g.getTailleGrille().getX());
		assigner("hauteur", (int)g.getTailleGrille().getY());
		assigner("posx", r.getX());
		assigner("posy", r.getY());
		assigner("orientation", r.getOrientation());
		assigner("gauche", 1);
		assigner("droite", -1);
		assigner("nord", Direction.Nord);
		assigner("ouest", Direction.Ouest);
		assigner("sud", Direction.Sud);
		assigner("est", Direction.Est);
		
		return this;
	}
	
	public Programme setConsole(Console c){
		_console = c;
		return this;
	}
	public Console getConsole(){
		return _console;
	}
	
	public Application getApp(){
		return _app;
	}
	
	// TODO Il y a beaucoup de chose à changer ici
	private static Instruction[] getInstructionsFromFichier(String adresse){
		return null;
	}
	
	public Instruction getProchaineInstruction(){
		return _instructions[_ligne++];
	}
	public int getLigne(){
		return _ligne;
	}
	public void setLigne(int ln){
		_ligne = ln;
	}
	
	public void assigner(String id, int n){
		_variables.put(id, n);
	}
	public int getVariable(String id){
		return _variables.get(id);
	}
	
}
