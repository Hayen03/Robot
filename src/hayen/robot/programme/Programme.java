package hayen.robot.programme;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Hashtable;

import hayen.robot.Direction;
import hayen.robot.Robot;
import hayen.robot.graphisme.Console;
import hayen.robot.graphisme.Grille;
import hayen.robot.programme.instruction.*;

public class Programme implements Runnable{
	
	private Grille _grille;
	private Console _console;
	private Thread _thread = null;
	private boolean _paused = false;
	
	protected final Instruction[] _instructions;
	protected Hashtable<String, Integer> _variables;
	protected int _ligne;
	protected final int _longueur;
	
	public Programme(Instruction[] instructions){
		
		_instructions = instructions;
		_variables = new Hashtable<String, Integer>();
		_ligne = 0;
		_longueur = _instructions.length;
		
		_grille = null;
		_console = null;
	}
	public Programme(String adresse) throws FileNotFoundException, IOException, ClassNotFoundException, FichierIncorrectException, OperationInvalideException{
		_instructions = getInstructionsFromFichier(adresse);
		
		_grille = null;
		_console = null;
	}
	public Programme(Bloc original){
		super(original);
		_grille = null;
		_console = null;
	}
	
	public Programme setGrille(Grille g){
		_grille = g;
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
	public Grille getGrille(){
		return _grille;
	}
	
	public Programme setConsole(Console c){
		_console = c;
		return this;
	}
	public Console getConsole(){
		return _console;
	}
	
	private static Instruction[] getInstructionsFromFichier(String adresse) throws IOException, FichierIncorrectException, ClassNotFoundException, OperationInvalideException{
		Instruction[] instructions;
		if (adresse.endsWith(".pr")){
			instructions = Compilateur.compileFichier(adresse);
		}
		else if (!adresse.endsWith(".prc")) throw new FichierIncorrectException("Fichier invalide");
		else{
			ObjectInputStream fichier = new ObjectInputStream(new FileInputStream(adresse));
			String version = fichier.readUTF();
			if (version.equals(Compilateur.version)) instructions = (Bloc)fichier.readObject();
			else{
				fichier.close();
				throw new FichierIncorrectException("Version trop ancienne du compilateur");
			}
			fichier.close();
		}
		System.out.println("fin compilation");
		return instructions;
	}
	
	@Override
	public boolean executer(Object... params){
		int i = 0;
		while (i < _instructions.length){
			if (!_paused){
//				System.out.print("l ");
				_instructions[i].executer(this);
				i++;
			}
		}
//		System.out.println("\nEnded;");
		return true;
	}
	
	@Override
	public void run() {
		executer();
	}
	public void start(){
//		System.out.print("Started: ");
		_paused = false;
		_thread = new Thread(this);
		_thread.start();
	}
	public void pause(){
		_paused = true;
//		System.out.println("paused;");
	}
	public void resume(){
		_paused = false;
//		System.out.print("resumed: ");
	}
	public boolean isPaused(){
		return _paused;
	}
	
}
