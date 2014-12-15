package hayen.robot.programme;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.io.InputStream;
import java.io.ObjectInputStream;

import hayen.robot.graphisme.Grille;
import hayen.robot.programme.instruction.*;

public class Programme extends Bloc{
	
	private Grille _grille = null;

	public Programme(Instruction[] instructions){
		super(instructions);
	}
	public Programme(String adresse) throws FileNotFoundException, IOException, ClassNotFoundException, FichierIncorrectException, OperationInvalideException{
		super(getInstructionsFromFichier(adresse));
	}
	public Programme(Bloc original){
		super(original);
	}
	
	public Programme setGrille(Grille g){
		_grille = g;
		assigner("largeur", g.getTailleGrille()[0]);
		assigner("hauteur", g.getTailleGrille()[1]);
		return this;
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
	
}
