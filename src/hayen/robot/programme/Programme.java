package hayen.robot.programme;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Hashtable;

import hayen.robot.programme.instruction.*;

public class Programme {
	private Instruction[] _instructions; // la liste d'instruction
	private Hashtable<String, Integer> _variables; // dictionaire des variables
	private int _ligne; // la ligne à laquelle le programme est rendu
	private int _indentation; // l'indentation du présent bloc d'instruction
	
	public Programme(Instruction[] instructions){
		_instructions = instructions;
		_variables = new Hashtable<String, Integer>();
		_ligne = 0;
		_indentation = 0;
	}
	
	public Programme(String adresse) throws FileNotFoundException, IOException, ClassNotFoundException, FichierIncorrectException{
		if (adresse.endsWith(".pr")){
			_instructions = Compilateur.compileFichier(adresse);
		}
		else if (!adresse.endsWith(".prc")) throw new FichierIncorrectException("Fichier invalide");
		else{
			ObjectInputStream fichier = new ObjectInputStream(new FileInputStream(adresse));
			_instructions = (Instruction[])fichier.readObject();
			fichier.close();
		}
		
		_variables = new Hashtable<String, Integer>();
		_ligne = 0;
		_indentation = 0;
	}
	
	public boolean executer(){
		while (_ligne < _instructions.length) {
	//		System.out.print("\tLIGNE " + (_ligne+1) + ">>> ");
			
			// détecteur de condition
			if (_instructions[_ligne].getClass().equals(Condition.class)){ // si c'est une condition...
	//			System.out.print("CONDITION ");
				_indentation = ((Condition)_instructions[_ligne]).getIndentation();
	//			System.out.print("(" + _indentation + ") ");
				if (!_instructions[_ligne].run(this)){ // ...on l'évalue. Si elle est fausse...
					// on cherche un sinon ou un fin
					while(_ligne < _instructions.length){
						_ligne++;
						Instruction i = _instructions[_ligne];
						if (i.getClass().equals(Fin.class) || i.getClass().equals(Sinon.class)) 
							if (i.run(this)) {
	//							System.out.print("FIN");
								break; // arrête la recherche si l'indentation est correcte && est fin
						}
					}
				}
//				_indentation = 0;
				// si elle est vrai, et on passe à la prochaine instruction
			}
			
			else if (_instructions[_ligne].getClass().equals(Sinon.class)){ // si c'est un sinon, mais que l'on était pas à la recherche d'un sinon, on skip jusqu'au fin correspondant
//				System.out.print("SINON ");
				_indentation = ((Sinon)_instructions[_ligne]).getIndentation();
				while(++_ligne < _instructions.length){
					Instruction i = _instructions[_ligne];
					if (i.getClass().equals(Fin.class)) if (i.run(this)) break; // arrête la recherche si l'indentation est correcte && est fin
				}
			}
			
			else { // si c'est une instruction normale
				_instructions[_ligne].run(this);
			}
			
			_ligne++;
		}
		return true;
	}
	
	public int getVariable(String id){
		return _variables.get(id).intValue();
	}
	
	public void assigner(String id, int valeur){
		_variables.put(id, valeur);
	}
	public void assigner(String id, Integer valeur){
		_variables.put(id, valeur);
	}
	
	public int getLigne() { return _ligne; }
	public void setLigne(int ln){
		_ligne = ln;
	}
	
	public int getIndentation(){
		return _indentation;
	}
	
	public void printInstruction(){
		for (Instruction i : _instructions) System.out.println(i);
	}
	
}
