package hayen.robot.programme;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.io.InputStream;
import java.io.ObjectInputStream;

import hayen.robot.programme.instruction.*;

public class Programme extends Bloc{
	
	protected final int[] _posRobot;

	public Programme(int x, int y, Instruction[] instructions){
		super(instructions);
		_posRobot = new int[2];
		_posRobot[0] = x;
		_posRobot[1] = y;
	}
	public Programme(Instruction[] instructions){
		this(0, 0, instructions);
	}
	public Programme(String adresse) throws FileNotFoundException, IOException, ClassNotFoundException, FichierIncorrectException, OperationInvalideException{
		super(getInstructionsFromFichier(adresse));
		_posRobot = new int[2];
		_posRobot[0] = 0;
		_posRobot[1] = 0;
	}
	public Programme(Bloc original){
		super(original);
		_posRobot = new int[2];
		_posRobot[0] = 0;
		_posRobot[1] = 0;
	}
	
	private static Bloc getInstructionsFromFichier(String adresse) throws IOException, FichierIncorrectException, ClassNotFoundException, OperationInvalideException{
		Bloc instructions;
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
		return instructions;
	}
	
	public int[] getPositionRobot(){
		return _posRobot;
	}
	
	public boolean deplacerRobot(int[] direction, int[] tailleGrille){
		boolean mouvementValide = true;
		
		_posRobot[0] += direction[0];
		_posRobot[1] += direction[1];
		
		if (_posRobot[0] < 0){
			_posRobot[0] = 0;
			mouvementValide = false;
		}
		else if (_posRobot[0] >= tailleGrille[0]){
			_posRobot[0] = tailleGrille[0] - 1;
			mouvementValide = false;
		}
		
		if (_posRobot[1] < 0){
			_posRobot[1] = 0;
			mouvementValide = false;
		}
		else if (_posRobot[1] >= tailleGrille[1]){
			_posRobot[1] = tailleGrille[1] - 1;
			mouvementValide = false;
		}
		
		return mouvementValide;	
	}
	
}
