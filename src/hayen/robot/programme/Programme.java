package hayen.robot.programme;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import hayen.robot.Direction;
import hayen.robot.Robot;
import hayen.robot.graphisme.Console;
import hayen.robot.graphisme.Grille;
import hayen.robot.programme.instruction.*;

public class Programme {
	
	private Application _app;
	private Console _console;
	
	private final Instruction[] _instructions;
	private Vector<Hashtable<String, Integer>> _variables;
	private int _ligne;
	private final int _longueur;
	private String _titre;
	
	public Programme(Instruction[] instructions, String titre){
		
		_instructions = instructions;
		_variables = new Vector<Hashtable<String, Integer>>();
		_variables.add(new Hashtable<String, Integer>());
		_ligne = 0;
		_longueur = _instructions.length;
		
		_console = null;
		_titre = titre;
	}
	public Programme(Instruction[] instructions){
		
		_instructions = instructions;
		_variables = new Vector<Hashtable<String, Integer>>();
		_variables.add(new Hashtable<String, Integer>());
		_ligne = 0;
		_longueur = _instructions.length;
		
		_console = null;
		_titre = "";
	}
	public Programme(String adresse) throws FileNotFoundException, IOException, ClassNotFoundException, FichierIncorrectException, OperationInvalideException{
		_instructions = getInstructionsFromFichier(adresse);
		_longueur = _instructions.length;
		_console = null;
		_titre = "";
	}
	
	public Programme setGrille(Grille g){ // TODO Il y aura surement des modifications à apporter ici
		Robot r = g.getRobot();
		_variables.get(0).put("largeur", (int)g.getTailleGrille().getX());
		_variables.get(0).put("hauteur", (int)g.getTailleGrille().getY());
		_variables.get(0).put("posx", r.getX());
		_variables.get(0).put("posy", r.getY());
		_variables.get(0).put("orientation", r.getOrientation());
		_variables.get(0).put("gauche", 1);
		_variables.get(0).put("droite", -1);
		_variables.get(0).put("nord", Direction.Nord);
		_variables.get(0).put("ouest", Direction.Ouest);
		_variables.get(0).put("sud", Direction.Sud);
		_variables.get(0).put("est", Direction.Est);
		
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
	
	public Programme setApp(Application app){
		_app = app;
		app.setTitle(_titre);
		return this;
	}
	
	public int longueur(){
		return _longueur;
	}
	public String getTitre(){
		return _titre;
	}
	public Programme setTitre(String titre){
		_titre = titre;
		_app.setTitle(titre);
		return this;
	}
	
	// TODO Il y a beaucoup de chose à changer ici
	private static Instruction[] getInstructionsFromFichier(String adresse){
		return null;
	}
	
	public Instruction getProchaineInstruction(){
		if (_ligne >= _instructions.length)
			return null;
		else
			return _instructions[_ligne++];
	}
	public Instruction getInstructionA(int ln){
		if (ln >= _instructions.length)
			return null;
		else
			return _instructions[ln];
	}
	public int getLigne(){
		return _ligne;
	}
	public void setLigne(int ln){
		_ligne = ln;
	}
	
	public void assigner(String id, int n){
		int i = 0;
		while (i < _variables.size()){
			if (_variables.get(i).contains(id)){
				_variables.get(i).put(id, n);
				return;
			}
			else
				i++;
		}
		_variables.get(i-1).put(id, n);
	}
	public int getVariable(String id){
		int i = 0;
		while (i < _variables.size()){
			if (_variables.get(i).contains(id))
				return _variables.get(i).get(id);
			else
				i++;
		}
		return _variables.get(i-1).get(id);
	}
	
	public Programme reset(){
		_ligne = 0;
		Hashtable<String, Integer> buffer = _variables.get(0);
		_variables = new Vector<Hashtable<String, Integer>>();
		_variables.add(buffer);
		return this;
	}
	
	public void incremente(){
		_variables.add(new Hashtable<String, Integer>());
	}
	public void decremente(){
		if (_variables.size() > 1)
			_variables.remove(_variables.size()-1);
	}
	
}
